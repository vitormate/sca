package rokaly.sca.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;
import rokaly.sca.dto.request.PickingOrderCreateRequest;
import rokaly.sca.dto.request.PickingProductsRequest;
import rokaly.sca.entity.PickingOrder;
import rokaly.sca.entity.Product;
import rokaly.sca.exception.BusinessException;
import rokaly.sca.repository.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PickingOrderServiceTest {

    @Mock
    StockRepository stockRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    PickingOrderRepository pickingOrderRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    MovementStockRepository movementStockRepository;

    @Mock
    PickingProductRepository pickingProductRepository;

    @InjectMocks
    PickingOrderService pickingOrderService;

    UriComponentsBuilder uriBuilder;
    PickingOrderCreateRequest pickingOrderCreateRequest;

    Product product1;
    Product product2;

    @BeforeEach
    void setUp() {
        this.uriBuilder = UriComponentsBuilder.newInstance();

        this.product1 = new Product("123AVC", "Basketball Ball", "UN");
        this.product1.setId(1L);
        this.product2 = new Product("ABC321", "Football Ball", "UN");
        this.product2.setId(2L);
    }

    @Nested
    class createOrderTests {

        @Test
        void shouldReturnStatusCode201() {
            PickingProductsRequest productRequest1 = new PickingProductsRequest(new BigDecimal(10), 1L);
            PickingProductsRequest productRequest2 = new PickingProductsRequest(new BigDecimal(10), 2L);
            List<PickingProductsRequest> pickingProducts = List.of(productRequest1, productRequest2);
            PickingOrderCreateRequest data = new PickingOrderCreateRequest("admin", pickingProducts);

            when(stockRepository.findPositionByProductId(any(Long.class))).thenReturn(List.of("AP01-01-01", "AP01-01-02"));
            when(productRepository.findById(any(Long.class))).thenAnswer(answer -> {
                Long id = answer.getArgument(0);
                return id == 1L ? Optional.of(product1) : Optional.of(product2);
            });

            int statusCode = pickingOrderService.createOrder(data, uriBuilder).getStatusCode().value();

            assertEquals(HttpStatus.CREATED.value(), statusCode);
            verify(pickingOrderRepository, times(1)).save(any(PickingOrder.class));
            verify(stockRepository, times(pickingProducts.size())).findPositionByProductId(any(Long.class));
            verify(productRepository, times(pickingProducts.size())).findById(any(Long.class));
            verifyNoInteractions(userRepository);
            verifyNoInteractions(movementStockRepository);
            verifyNoInteractions(pickingProductRepository);
        }

        @Test
        void shouldReturnBusinessExceptionWhenPickingProductsAreEquals() {
            PickingProductsRequest productRequest1 = new PickingProductsRequest(new BigDecimal(10), 1L);
            PickingProductsRequest productRequest2 = new PickingProductsRequest(new BigDecimal(10), 1L);
            List<PickingProductsRequest> pickingProducts = List.of(productRequest1, productRequest2);
            PickingOrderCreateRequest data = new PickingOrderCreateRequest("admin", pickingProducts);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> pickingOrderService.createOrder(data, uriBuilder)
            );

            assertEquals("The products must be different in a Picking Order!", exception.getMessage());
            verifyNoInteractions(pickingOrderRepository);
            verifyNoInteractions(stockRepository);
            verifyNoInteractions(productRepository);
            verifyNoInteractions(userRepository);
            verifyNoInteractions(movementStockRepository);
            verifyNoInteractions(pickingProductRepository);
        }

        @Test
        void shouldReturnEntityNotFoundExceptionWhenPositionNotFound() {
            PickingProductsRequest productRequest1 = new PickingProductsRequest(new BigDecimal(10), 1L);
            PickingProductsRequest productRequest2 = new PickingProductsRequest(new BigDecimal(10), 2L);
            List<PickingProductsRequest> pickingProducts = List.of(productRequest1, productRequest2);
            PickingOrderCreateRequest data = new PickingOrderCreateRequest("admin", pickingProducts);

            when(stockRepository.findPositionByProductId(any(Long.class))).thenReturn(List.of());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> pickingOrderService.createOrder(data, uriBuilder)
            );

            assertEquals("Position not found with id: 1", exception.getMessage());
            verify(stockRepository, times(1)).findPositionByProductId(any(Long.class));
            verifyNoInteractions(pickingOrderRepository);
            verifyNoInteractions(productRepository);
            verifyNoInteractions(userRepository);
            verifyNoInteractions(movementStockRepository);
            verifyNoInteractions(pickingProductRepository);
        }

        @Test
        void shouldReturnEntityNotFoundWhenProductNotFound() {
            PickingProductsRequest productRequest1 = new PickingProductsRequest(new BigDecimal(10), 1L);
            PickingProductsRequest productRequest2 = new PickingProductsRequest(new BigDecimal(10), 2L);
            List<PickingProductsRequest> pickingProducts = List.of(productRequest1, productRequest2);
            PickingOrderCreateRequest data = new PickingOrderCreateRequest("admin", pickingProducts);

            when(stockRepository.findPositionByProductId(any(Long.class))).thenReturn(List.of("AP01-01-01", "AP01-01-02"));
            when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> pickingOrderService.createOrder(data, uriBuilder)
            );

            assertEquals("Product not found with id: 1", exception.getMessage());
            verify(stockRepository, times(1)).findPositionByProductId(any(Long.class));
            verify(productRepository, times(1)).findById(any(Long.class));
            verifyNoInteractions(pickingOrderRepository);
            verifyNoInteractions(userRepository);
            verifyNoInteractions(movementStockRepository);
            verifyNoInteractions(pickingProductRepository);
        }
    }

    @Test
    void assignOrder() {
    }

    @Test
    void getAllProductsFromOrder() {
    }

    @Test
    void collectProduct() {
    }

    @Test
    void finishOrderWithPartialCollection() {
    }

    @Test
    void cancelOrder() {
    }
}