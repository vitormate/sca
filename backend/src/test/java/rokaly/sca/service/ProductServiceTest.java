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
import rokaly.sca.dto.request.UpdateProductRequest;
import rokaly.sca.entity.Product;
import rokaly.sca.repository.ProductRepository;
import rokaly.sca.utils.enums.StatusProduct;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    Product product;
    UpdateProductRequest update;

    @BeforeEach
    void setUp() {
        this.product = new Product("123AV32", "Ball", "UN");

        this.update = new UpdateProductRequest(1L, null, null, StatusProduct.INACTIVE);
    }

    @Nested
    class putProductTests {


        @Test
        void shouldReturnStatusCode200() {

            when(productRepository.findById(1L)).thenReturn(Optional.of(product));

            int statusCode = productService.putProductService(update).getStatusCode().value();

            assertEquals(HttpStatus.OK.value(), statusCode);
        }

        @Test
        void shouldReturnEntityNotFoundWhenProductNotFound() {
            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            final EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> productService.putProductService(update)
            );

            assertEquals("Product not found with id: " + update.id(), exception.getMessage());
        }
    }

    @Nested
    class deleteLogicTests {

        @Test
        void shouldReturnStatusCode204() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));

            int statusCode = productService.deleteLogicProductService(1L).getStatusCode().value();

            assertEquals(HttpStatus.NO_CONTENT.value(), statusCode);
        }

        @Test
        void shouldReturnEntityNotFoundWhenProductNotFound() {
            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            final EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> productService.deleteLogicProductService(1L)
            );

            assertEquals("Product not found with id: 1", exception.getMessage());
        }
    }


}