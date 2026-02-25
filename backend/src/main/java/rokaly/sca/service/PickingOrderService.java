package rokaly.sca.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import rokaly.sca.dto.PickingOrderCreateRequest;
import rokaly.sca.entity.PickingOrder;
import rokaly.sca.entity.PickingProduct;
import rokaly.sca.entity.Product;
import rokaly.sca.repository.PickingOrderRepository;
import rokaly.sca.repository.PickingProductRepository;
import rokaly.sca.repository.ProductRepository;
import rokaly.sca.repository.StockRepository;
import rokaly.sca.utils.enums.PickingOrderStatus;

import java.time.LocalDateTime;

@Service
public class PickingOrderService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final PickingOrderRepository pickingOrderRepository;

    public PickingOrderService(StockRepository stockRepository, ProductRepository productRepository, PickingOrderRepository pickingOrderRepository, PickingProductRepository pickingProductRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.pickingOrderRepository = pickingOrderRepository;
    }

    public ResponseEntity<Void> createOrder(PickingOrderCreateRequest data, UriComponentsBuilder uriBuilder) {
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
}
