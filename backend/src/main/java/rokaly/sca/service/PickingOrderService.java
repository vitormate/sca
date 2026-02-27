package rokaly.sca.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rokaly.sca.dto.PickingOrderAssignRequest;
import rokaly.sca.dto.PickingOrderCreateRequest;
import rokaly.sca.dto.PickingOrderResponse;
import rokaly.sca.entity.PickingOrder;
import rokaly.sca.entity.PickingProduct;
import rokaly.sca.entity.Product;
import rokaly.sca.entity.User;
import rokaly.sca.repository.*;
import rokaly.sca.utils.enums.PickingOrderStatus;

import java.time.LocalDateTime;

@Service
public class PickingOrderService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final PickingOrderRepository pickingOrderRepository;
    private final UserRepository userRepository;

    public PickingOrderService(StockRepository stockRepository, ProductRepository productRepository, PickingOrderRepository pickingOrderRepository, PickingProductRepository pickingProductRepository, UserRepository userRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.pickingOrderRepository = pickingOrderRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Void> createOrder(PickingOrderCreateRequest data) {
        PickingOrder.validProducts(data.pickingProducts());

        PickingOrder pickingOrder = new PickingOrder(data.createdBy(), LocalDateTime.now(), PickingOrderStatus.CREATED);

        data.pickingProducts().forEach(p -> {
            String suggestedPosition = stockRepository
                    .findPositionByProductId(p.productId())
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + p.productId()));

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

    public ResponseEntity<Void> assignOrder(PickingOrderAssignRequest data) {
        PickingOrder pickingOrder = pickingOrderRepository.findById(data.pickingOrderId()).orElseThrow(
                () -> new RuntimeException("Picking Order not found with id: " + data.pickingOrderId())
        );

        User user = userRepository.findByUsername(data.separator());
        pickingOrder.setSeparator(user);
        pickingOrder.setAssignedAt(LocalDateTime.now());
        pickingOrder.setStatus(PickingOrderStatus.ASSIGNED);

        return ResponseEntity.ok().build();
    }
}
