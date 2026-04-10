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
import rokaly.sca.dto.request.PickingOrderAssignRequest;
import rokaly.sca.dto.request.PickingOrderCreateRequest;
import rokaly.sca.dto.request.PickingProductsCollectRequest;
import rokaly.sca.dto.request.PickingProductsRequest;
import rokaly.sca.entity.*;
import rokaly.sca.exception.BusinessException;
import rokaly.sca.repository.*;
import rokaly.sca.utils.enums.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    PickingOrder pickingOrder;
    User user;
    PickingProduct pickingProduct;
    Position position;

    @BeforeEach
    void setUp() {
        this.uriBuilder = UriComponentsBuilder.newInstance();

        this.product1 = new Product("123AVC", "Basketball Ball", "UN");
        this.product1.setId(1L);
        this.product2 = new Product("ABC321", "Football Ball", "UN");
        this.product2.setId(2L);

        this.user = new User("separator", "123456", Role.SEPARATOR);

        this.pickingOrder = new PickingOrder("admin", LocalDateTime.now().minusHours(1), PickingOrderStatus.CREATED);
        this.pickingOrder.setId(1L);
        this.pickingProduct = new PickingProduct(new BigDecimal(10), "AP01-01-01", product1, pickingOrder);
        List<PickingProduct> pickingProducts = List.of(pickingProduct);
        this.pickingOrder.setPickingProducts(pickingProducts);

        this.position = new Position("AP01-01-01");
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

    @Nested
    class AssignOrderTests {

        @Test
        void shouldReturnStatusCode200() {
            PickingOrderAssignRequest data = new PickingOrderAssignRequest("admin");
            when(pickingOrderRepository.findById(1L)).thenReturn(Optional.of(pickingOrder));
            when(userRepository.findByUsername(data.separator())).thenReturn(Optional.of(user));

            int statusCode = pickingOrderService.assignOrder(1L, data).getStatusCode().value();

            assertEquals(HttpStatus.OK.value(), statusCode);
            verify(pickingOrderRepository, times(1)).findById(any(Long.class));
            verify(userRepository, times(1)).findByUsername(any(String.class));
            verifyNoInteractions(stockRepository);
            verifyNoInteractions(productRepository);
            verifyNoInteractions(movementStockRepository);
            verifyNoInteractions(pickingProductRepository);
        }

        @Test
        void shouldReturnEntityNotFoundExceptionWhenPickingOrderNotFound() {
            PickingOrderAssignRequest data = new PickingOrderAssignRequest("admin");

            when(pickingOrderRepository.findById(1L)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> pickingOrderService.assignOrder(1L, data)
            );

            assertEquals("Picking Order not found with id: 1", exception.getMessage());
            verify(pickingOrderRepository, times(1)).findById(any(Long.class));
            verifyNoInteractions(userRepository);
            verifyNoInteractions(stockRepository);
            verifyNoInteractions(productRepository);
            verifyNoInteractions(movementStockRepository);
            verifyNoInteractions(pickingProductRepository);
        }

        @Test
        void shouldReturnEntityNotFoundExceptionWhenUserNotFound() {
            PickingOrderAssignRequest data = new PickingOrderAssignRequest("admin");

            when(pickingOrderRepository.findById(1L)).thenReturn(Optional.of(pickingOrder));
            when(userRepository.findByUsername(data.separator())).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> pickingOrderService.assignOrder(1L, data)
            );

            assertEquals("User not found with id: 1", exception.getMessage());
            verify(pickingOrderRepository, times(1)).findById(any(Long.class));
            verify(userRepository, times(1)).findByUsername(any(String.class));
            verifyNoInteractions(stockRepository);
            verifyNoInteractions(productRepository);
            verifyNoInteractions(movementStockRepository);
            verifyNoInteractions(pickingProductRepository);
        }
    }

    @Nested
    class GetAllProductsFromOrderTests {

        @Test
        void shouldReturnStatusCode200() {
            when(pickingOrderRepository.findById(any(Long.class))).thenReturn(Optional.of(pickingOrder));

            int statusCode = pickingOrderService.getAllProductsFromOrder(1L).getStatusCode().value();

            assertEquals(HttpStatus.OK.value(), statusCode);
            verify(pickingOrderRepository, times(1)).findById(any(Long.class));
            verifyNoInteractions(stockRepository);
            verifyNoInteractions(userRepository);
            verifyNoInteractions(productRepository);
            verifyNoInteractions(movementStockRepository);
            verifyNoInteractions(pickingProductRepository);
        }

        @Test
        void shouldReturnEntityNotFoundExceptionWhenPickingOrderNotFound() {
            when(pickingOrderRepository.findById(any(Long.class))).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> pickingOrderService.getAllProductsFromOrder(1L)
            );

            assertEquals("Order not found with id: 1", exception.getMessage());
        }
    }

    @Nested
    class CollectProductTests {

        @Test
        void shouldReturnStatusCode200() {
            PickingProductsCollectRequest data = new PickingProductsCollectRequest("AP01-01-01");
            Stock stock = new Stock(product1, position, new BigDecimal(100));

            when(stockRepository.findByPositionCode(data.positionCode())).thenReturn(Optional.of(stock));
            when(pickingOrderRepository.findById(any(Long.class))).thenReturn(Optional.of(pickingOrder));
            when(pickingProductRepository.findByIdAndOrderId(any(Long.class), any(Long.class))).thenReturn(Optional.of(pickingProduct));

            int statusCode = pickingOrderService.collectProduct(1L, 1L, data).getStatusCode().value();

            assertEquals(HttpStatus.OK.value(), statusCode);
            verify(stockRepository, times(1)).findByPositionCode(any(String.class));
            verify(pickingOrderRepository, times(1)).findById(any(Long.class));
            verify(pickingProductRepository, times(1)).findByIdAndOrderId(any(Long.class), any(Long.class));
            verify(movementStockRepository, times(1)).save(any(MovementStock.class));
            verifyNoInteractions(userRepository);
            verifyNoInteractions(productRepository);
        }

        @Test
        void shouldReturnEntityNotFoundExceptionWhenStockNotFound() {
            PickingProductsCollectRequest data = new PickingProductsCollectRequest("AP01-01-01");

            when(stockRepository.findByPositionCode(data.positionCode())).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> pickingOrderService.collectProduct(1L, 1L, data)
            );

            assertEquals("Stock not found with position: " + data.positionCode(), exception.getMessage());
            verify(stockRepository, times(1)).findByPositionCode(any(String.class));
            verifyNoInteractions(userRepository);
            verifyNoInteractions(productRepository);
            verifyNoInteractions(pickingOrderRepository);
            verifyNoInteractions(pickingProductRepository);
            verifyNoInteractions(movementStockRepository);
        }

        @Test
        void shouldReturnBusinessExceptionWhenStockAmountLessOrEqualZero() {
            PickingProductsCollectRequest data = new PickingProductsCollectRequest("AP01-01-01");
            Stock stock = new Stock(product1, position, new BigDecimal(0));

            when(stockRepository.findByPositionCode(data.positionCode())).thenReturn(Optional.of(stock));

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> pickingOrderService.collectProduct(1L, 1L, data)
            );

            assertEquals("Insufficient stock! Stock: " + stock.getAmount(), exception.getMessage());
            verify(stockRepository, times(1)).findByPositionCode(any(String.class));
            verifyNoInteractions(userRepository);
            verifyNoInteractions(productRepository);
            verifyNoInteractions(pickingOrderRepository);
            verifyNoInteractions(pickingProductRepository);
            verifyNoInteractions(movementStockRepository);
        }

        @Test
        void shouldReturnEntityNotFoundExceptionWhenOrderNotFound() {
            PickingProductsCollectRequest data = new PickingProductsCollectRequest("AP01-01-01");
            Stock stock = new Stock(product1, position, new BigDecimal(100));

            when(stockRepository.findByPositionCode(data.positionCode())).thenReturn(Optional.of(stock));
            when(pickingOrderRepository.findById(any(Long.class))).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> pickingOrderService.collectProduct(1L, 1L, data)
            );

            assertEquals("Order not found with id: 1", exception.getMessage());
            verify(stockRepository, times(1)).findByPositionCode(any(String.class));
            verify(pickingOrderRepository, times(1)).findById(any(Long.class));
            verifyNoInteractions(userRepository);
            verifyNoInteractions(productRepository);
            verifyNoInteractions(pickingProductRepository);
            verifyNoInteractions(movementStockRepository);
        }

        @Test
        void shouldReturnEntityNotFoundWhenProductNotFound() {
            PickingProductsCollectRequest data = new PickingProductsCollectRequest("AP01-01-01");
            Stock stock = new Stock(product1, position, new BigDecimal(100));

            when(stockRepository.findByPositionCode(data.positionCode())).thenReturn(Optional.of(stock));
            when(pickingOrderRepository.findById(any(Long.class))).thenReturn(Optional.of(pickingOrder));
            when(pickingProductRepository.findByIdAndOrderId(any(Long.class), any(Long.class))).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> pickingOrderService.collectProduct(1L, 1L, data)
            );

            assertEquals("Picking Product not found with id: 1", exception.getMessage());
            verify(stockRepository, times(1)).findByPositionCode(any(String.class));
            verify(pickingOrderRepository, times(1)).findById(any(Long.class));
            verify(pickingProductRepository, times(1)).findByIdAndOrderId(any(Long.class), any(Long.class));
            verifyNoInteractions(userRepository);
            verifyNoInteractions(productRepository);
            verifyNoInteractions(movementStockRepository);
        }

        @Test
        void shouldReturnBusinessExceptionWhenPickingProductNotEqualStockProduct() {
            PickingProductsCollectRequest data = new PickingProductsCollectRequest("AP01-01-01");
            Stock stock = new Stock(product1, position, new BigDecimal(100));
            pickingProduct.setProduct(product2);

            when(stockRepository.findByPositionCode(data.positionCode())).thenReturn(Optional.of(stock));
            when(pickingOrderRepository.findById(any(Long.class))).thenReturn(Optional.of(pickingOrder));
            when(pickingProductRepository.findByIdAndOrderId(any(Long.class), any(Long.class))).thenReturn(Optional.of(pickingProduct));

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> pickingOrderService.collectProduct(1L, 1L, data)
            );

            assertEquals("Product on system different of product on position! " + pickingProduct.getProduct().getCode() + " != " + stock.getProduct().getCode(), exception.getMessage());
            verify(stockRepository, times(1)).findByPositionCode(any(String.class));
            verify(pickingOrderRepository, times(1)).findById(any(Long.class));
            verify(pickingProductRepository, times(1)).findByIdAndOrderId(any(Long.class), any(Long.class));
            verifyNoInteractions(userRepository);
            verifyNoInteractions(productRepository);
            verifyNoInteractions(movementStockRepository);
        }

        @Test
        void shouldReturnBusinessExceptionWhenProductAlreadyCollected() {
            PickingProductsCollectRequest data = new PickingProductsCollectRequest("AP01-01-01");
            Stock stock = new Stock(product1, position, new BigDecimal(100));
            pickingProduct.setStatusCollected(PickingProductCollectedStatus.COMPLETED);

            when(stockRepository.findByPositionCode(data.positionCode())).thenReturn(Optional.of(stock));
            when(pickingOrderRepository.findById(any(Long.class))).thenReturn(Optional.of(pickingOrder));
            when(pickingProductRepository.findByIdAndOrderId(any(Long.class), any(Long.class))).thenReturn(Optional.of(pickingProduct));

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> pickingOrderService.collectProduct(1L, 1L, data)
            );

            assertEquals("Product already collected!", exception.getMessage());
            verify(stockRepository, times(1)).findByPositionCode(any(String.class));
            verify(pickingOrderRepository, times(1)).findById(any(Long.class));
            verify(pickingProductRepository, times(1)).findByIdAndOrderId(any(Long.class), any(Long.class));
            verifyNoInteractions(userRepository);
            verifyNoInteractions(productRepository);
            verifyNoInteractions(movementStockRepository);
        }
    }

    @Test
    void finishOrderWithPartialCollection() {
    }

    @Test
    void cancelOrder() {
    }
}