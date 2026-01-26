package rokaly.sca.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public ResponseEntity<ProductResponse> createProductService(ProductResquest data, UriComponentsBuilder uriBuilder) {
        Product product = new Product(data.code(), data.description(), data.unit());
        repository.save(product);
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        ProductResponse dto = new ProductResponse(product.getId(), data.code(), data.description(), data.unit(), product.getStatus());
        return ResponseEntity.created(uri).body(dto);
    }

    public ResponseEntity<Page<ProductResponse>> getAllService(Pageable pagination) {
        Page<ProductResponse> page = repository.findAll(pagination).map(p -> new ProductResponse(p.getId(), p.getCode(), p.getDescription(), p.getUnit(), p.getStatus()));
        return ResponseEntity.ok(page);
    }
}
