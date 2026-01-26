package rokaly.sca.controller;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        return productService.createProductService(data, uriBuilder);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAll(@PageableDefault(size = 10, page = 0, sort = {"id"}) Pageable pagination) {
        return productService.getAllService(pagination);
    }
}
