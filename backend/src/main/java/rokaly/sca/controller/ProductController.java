package rokaly.sca.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import rokaly.sca.dto.ProductResponse;
import rokaly.sca.dto.ProductResquest;
import rokaly.sca.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductResquest data, UriComponentsBuilder uriBuilder) {
        return productService.createProduct(data, uriBuilder);
    }
}
