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
import rokaly.sca.dto.request.StockAdjustmentRequest;
import rokaly.sca.dto.request.StockEntryRequest;
import rokaly.sca.entity.MovementStock;
import rokaly.sca.entity.Position;
import rokaly.sca.entity.Product;
import rokaly.sca.entity.Stock;
import rokaly.sca.exception.BusinessException;
import rokaly.sca.repository.MovementStockRepository;
import rokaly.sca.repository.PositionRepository;
import rokaly.sca.repository.ProductRepository;
import rokaly.sca.repository.StockRepository;
import rokaly.sca.utils.enums.StatusPosition;
import rokaly.sca.utils.enums.StatusProduct;
import rokaly.sca.utils.mappers.MovementStockMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    StockRepository stockRepository;

    @Mock
    MovementStockRepository movementStockRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    PositionRepository positionRepository;

    @InjectMocks
    StockService stockService;

    Product product;
    Position position;
    MovementStock movementStock;
    UriComponentsBuilder uriBuilder;
    Stock stock;

    @BeforeEach
    void setUp() {
        this.product = new Product("123AV32", "Ball", "UN");
        this.position = new Position("AP01-01-02");
        this.movementStock = MovementStockMapper.createIn(product.getCode(), product.getName(), position.getCode(), new BigDecimal(10), "admin", "s");
        this.uriBuilder = UriComponentsBuilder.newInstance();
        this.stock = new Stock(product, position, new BigDecimal(100));
    }

    @Nested
    class createEntriesTests {

        @Test
        void shouldReturnStatusCode201() {
            StockEntryRequest data = new StockEntryRequest(1L, 1L, new BigDecimal(25), "admin", null);
            Stock stock = new Stock(product, position, data.amount());

            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(positionRepository.findById(1L)).thenReturn(Optional.of(position));

            int statusCode = stockService.createEntries(data, uriBuilder).getStatusCode().value();

            assertEquals(HttpStatus.CREATED.value(), statusCode);
            verify(productRepository, times(1)).findById(1L);
            verify(positionRepository, times(1)).findById(1L);
            verify(stockRepository, times(1)).save(any(Stock.class));
            verify(movementStockRepository, times(1)).save(any(MovementStock.class));
        }

        @Test
        void shouldReturnBusinessExceptionWhenAmountLowerThanZero() {
            StockEntryRequest data = new StockEntryRequest(1L, 1L, new BigDecimal(-25), "admin", null);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> stockService.createEntries(data, uriBuilder)
            );

            assertEquals("Amount need to be bigger than 0(zero). Amount: " + data.amount(), exception.getMessage());
            verifyNoInteractions(productRepository);
            verifyNoInteractions(positionRepository);
            verifyNoInteractions(stockRepository);
            verifyNoInteractions(movementStockRepository);
        }

        @Test
        void shouldReturnEntityNotFoundExceptionWhenProductNotFound() {
            StockEntryRequest data = new StockEntryRequest(1L, 1L, new BigDecimal(25), "admin", null);

            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> stockService.createEntries(data, uriBuilder)
            );

            assertEquals("Product not found with id: " + data.productId(), exception.getMessage());
            verify(productRepository, times(1)).findById(1L);
            verifyNoInteractions(positionRepository);
            verifyNoInteractions(stockRepository);
            verifyNoInteractions(movementStockRepository);
        }

        @Test
        void shouldReturnBusinessExceptionWhenProductNotActive() {
            product.setStatus(StatusProduct.INACTIVE);
            StockEntryRequest data = new StockEntryRequest(1L, 1L, new BigDecimal(25), "admin", null);

            when(productRepository.findById(1L)).thenReturn(Optional.of(product));

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> stockService.createEntries(data, uriBuilder)
            );

            assertEquals("Product is not active. Status: " + product.getStatus(), exception.getMessage());
            verify(productRepository, times(1)).findById(1L);
            verifyNoInteractions(positionRepository);
            verifyNoInteractions(stockRepository);
            verifyNoInteractions(movementStockRepository);
        }

        @Test
        void shouldReturnBusinessExceptionWhenPositionNotFound() {
            StockEntryRequest data = new StockEntryRequest(1L, 1L, new BigDecimal(25), "admin", null);

            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(positionRepository.findById(1L)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> stockService.createEntries(data, uriBuilder)
            );

            assertEquals("Position not found with id: " + data.positionId(), exception.getMessage());
            verify(productRepository, times(1)).findById(1L);
            verify(positionRepository, times(1)).findById(1L);
            verifyNoInteractions(stockRepository);
            verifyNoInteractions(movementStockRepository);
        }

        @Test
        void shouldReturnBusinessExceptionWhenPositionNotActive() {
            position.setStatus(StatusPosition.INACTIVE);
            StockEntryRequest data = new StockEntryRequest(1L, 1L, new BigDecimal(25), "admin", null);

            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(positionRepository.findById(1L)).thenReturn(Optional.of(position));

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> stockService.createEntries(data, uriBuilder)
            );

            assertEquals("Position not active. Status: " + position.getStatus(), exception.getMessage());
            verify(productRepository, times(1)).findById(1L);
            verify(positionRepository, times(1)).findById(1L);
            verifyNoInteractions(stockRepository);
            verifyNoInteractions(movementStockRepository);
        }
    }

    @Nested
    class createAdjustmentTests {

        @Test
        void shouldReturnStatusCode200() {
            StockAdjustmentRequest data = new StockAdjustmentRequest(1L, new BigDecimal(25), "admin", "Transfer");

            when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));

            int statusCode = stockService.createAdjustment(data).getStatusCode().value();

            assertEquals(HttpStatus.OK.value(), statusCode);
            verify(stockRepository, times(1)).findById(1L);
            verify(movementStockRepository, times(1)).save(any(MovementStock.class));
        }

        @Test
        void shouldReturnBusinessExceptionWhenAmountLowerThanZero() {
            StockAdjustmentRequest data = new StockAdjustmentRequest(1L, new BigDecimal(-25), "admin", "Transfer");

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> stockService.createAdjustment(data)
            );

            assertEquals("Amount need to be bigger than 0(zero). Amount: " + data.newAmount(), exception.getMessage());
            verifyNoInteractions(stockRepository);
            verifyNoInteractions(movementStockRepository);
        }

        @Test
        void shouldReturnEntityNotFoundWhenStockNotFound() {
            StockAdjustmentRequest data = new StockAdjustmentRequest(1L, new BigDecimal(25), "admin", "Transfer");

            when(stockRepository.findById(1L)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> stockService.createAdjustment(data)
            );

            assertEquals("Stock not found with id: " + data.id(), exception.getMessage());
            verify(stockRepository, times(1)).findById(1L);
            verifyNoInteractions(movementStockRepository);
        }
    }
}