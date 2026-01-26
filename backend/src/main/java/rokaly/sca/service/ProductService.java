package rokaly.sca.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import rokaly.sca.dto.ProductResponse;
import rokaly.sca.dto.ProductResquest;
import rokaly.sca.entity.Product;
import rokaly.sca.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository productRepository) {
        this.repository = productRepository;
    }

    public ResponseEntity<ProductResponse> createProduct(ProductResquest data, UriComponentsBuilder uriBuilder) {
        Product product = new Product(data.code(), data.description(), data.unit());
        repository.save(product);
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        ProductResponse dto = new ProductResponse(product.getId(), data, product.getStatus());
        return ResponseEntity.created(uri).body(dto);
    }
}
