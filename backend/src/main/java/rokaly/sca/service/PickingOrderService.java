package rokaly.sca.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rokaly.sca.dto.request.PickingOrderAssignRequest;
import rokaly.sca.dto.request.PickingOrderCancelRequest;
import rokaly.sca.dto.request.PickingOrderCreateRequest;
import rokaly.sca.dto.request.PickingProductsCollectRequest;
import rokaly.sca.dto.response.PickingOrderResponse;
import rokaly.sca.dto.response.PickingProductsResponse;
import rokaly.sca.entity.*;
import rokaly.sca.repository.*;
import rokaly.sca.utils.enums.PickingOrderStatus;
import rokaly.sca.utils.enums.PickingProductCollectedStatus;
import rokaly.sca.utils.enums.PickingProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PickingOrderService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final PickingOrderRepository pickingOrderRepository;
    private final UserRepository userRepository;
    private final MovementStockRepository movementStockRepository;
    private final PickingProductRepository pickingProductRepository;

    public PickingOrderService(StockRepository stockRepository, ProductRepository productRepository, PickingOrderRepository pickingOrderRepository, UserRepository userRepository, MovementStockRepository movementStockRepository, PickingProductRepository pickingProductRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.pickingOrderRepository = pickingOrderRepository;
        this.userRepository = userRepository;
        this.movementStockRepository = movementStockRepository;
        this.pickingProductRepository = pickingProductRepository;
    }

    public ResponseEntity<Void> createOrder(PickingOrderCreateRequest data) {
        PickingOrder.validProducts(data.pickingProducts());

        PickingOrder pickingOrder = new PickingOrder(data.createdBy(), LocalDateTime.now(), PickingOrderStatus.CREATED);

        data.pickingProducts().forEach(p -> {
            String suggestedPosition = stockRepository
                    .findPositionByProductId(p.productId())
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + p.productId()));

            Product product = productRepository.findById(p.productId()).orElseThrow(
                    () -> new EntityNotFoundException("Product not found with id: " + p.productId())
            );
            PickingProduct pickingProduct = new PickingProduct(p.requestedAmount(), suggestedPosition, product, pickingOrder);

            pickingOrder.getPickingProducts().add(pickingProduct);
        });

        pickingOrderRepository.save(pickingOrder);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Page<PickingOrderResponse>> getAll(Pageable pagination) {
        Page<PickingOrderResponse> order = pickingOrderRepository.findAll(pagination).map(PickingOrderResponse::new);
        return ResponseEntity.ok(order);
    }

    public ResponseEntity<Void> assignOrder(Long id, PickingOrderAssignRequest data) {
        PickingOrder pickingOrder = pickingOrderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Picking Order not found with id: " + id)
        );

        User user = userRepository.findByUsername(data.separator());
        pickingOrder.setSeparator(user);
        pickingOrder.setAssignedAt(LocalDateTime.now());
        pickingOrder.setStatus(PickingOrderStatus.ASSIGNED);

        pickingOrder.getPickingProducts().forEach(p -> {
            p.setStatus(PickingProductStatus.PICKING);
            p.setStatusCollected(PickingProductCollectedStatus.IN_PROGRESS);
        });

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<PickingProductsResponse>> getAllProductsFromOrder(Long orderId) {
        PickingOrder pickingOrder = pickingOrderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order not found with id: " + orderId)
        );

        List<PickingProductsResponse> products = pickingOrder.getPickingProducts().stream().map(PickingProductsResponse::new).toList();

        return ResponseEntity.ok(products);
    }

    public ResponseEntity<PickingProductsResponse> collectProduct(Long orderId, Long pickingProductId, PickingProductsCollectRequest data) {
        Stock stock = stockRepository.findByPositionCode(data.positionCode()).orElseThrow(
                () -> new EntityNotFoundException("Stock not found with position: " + data.positionCode())
        );

        stock.validStock();

        PickingOrder pickingOrder = pickingOrderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order not found with id: " + orderId)
        );


        PickingProduct pickingProduct = pickingProductRepository.findByIdAndOrderId(pickingProductId, orderId).orElseThrow(
                () -> new EntityNotFoundException("Picking Product not found with id: " + pickingProductId)
        );

        pickingProduct.validProduct(stock.getProduct().getCode());
        BigDecimal collectNow = pickingProduct.collectProduct(stock.getAmount());
        stock.collectAmount(collectNow);

        MovementStock movementStock = MovementStock.createOut(pickingProduct.getProduct().getCode(), pickingProduct.getProduct().getName(), data.positionCode(), collectNow, pickingOrder.getCreatedBy());
        movementStockRepository.save(movementStock);

        pickingOrder.checkAndFinish();

        PickingProductsResponse product = new PickingProductsResponse(pickingProduct);

        return ResponseEntity.ok(product);
    }

    public ResponseEntity<PickingOrderResponse> finishOrderWithPartialCollection(Long orderId) {
        PickingOrder pickingOrder = pickingOrderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order not found with id: " + orderId)
        );

        pickingOrder.setFinishedAt(LocalDateTime.now());
        pickingOrder.setStatus(PickingOrderStatus.FINISHED);

        pickingOrder.getPickingProducts().forEach(p -> {
            p.setStatus(PickingProductStatus.COLLECTED);
        });

        PickingOrderResponse orderResponse = new PickingOrderResponse(pickingOrder);

        return ResponseEntity.ok(orderResponse);
    }

    public ResponseEntity<PickingOrderResponse> cancelOrder(Long orderId, PickingOrderCancelRequest data) {
        PickingOrder pickingOrder = pickingOrderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order not found with id: " + orderId)
        );

        pickingOrder.setCanceledAt(LocalDateTime.now());
        pickingOrder.setStatus(PickingOrderStatus.CANCELED);

        pickingOrder.getPickingProducts().forEach(p -> {
            if (p.getCollectedAmount().compareTo(BigDecimal.ZERO) > 0) {
                Stock stock = stockRepository.findByProductCodeAndPositionCode(p.getProduct().getCode(), p.getSuggestedPosition()).orElseThrow(
                        () -> new EntityNotFoundException("Stock not found with product code and position code: " + p.getProduct().getCode() + " | " + p.getSuggestedPosition())
                );

                stock.addAmount(p.getCollectedAmount());

                MovementStock movementStock = MovementStock.createIn(p.getProduct().getCode(), p.getProduct().getName(), p.getSuggestedPosition(), p.getCollectedAmount(), data.name(), data.reason());
            }

            p.setStatus(PickingProductStatus.CANCELED);
        });

        PickingOrderResponse orderResponse = new PickingOrderResponse(pickingOrder);

        return ResponseEntity.ok(orderResponse);
    }
}
