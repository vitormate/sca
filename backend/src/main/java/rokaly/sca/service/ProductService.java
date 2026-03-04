package rokaly.sca.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import rokaly.sca.dto.response.ProductResponse;
import rokaly.sca.dto.request.ProductResquest;
import rokaly.sca.dto.request.UpdateProductRequest;
import rokaly.sca.entity.Product;
import rokaly.sca.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository productRepository) {
        this.repository = productRepository;
    }

    public ResponseEntity<ProductResponse> createProductService(ProductResquest data, UriComponentsBuilder uriBuilder) {
        Product product = new Product(data.code(), data.name(), data.unit());
        repository.save(product);
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        ProductResponse dto = new ProductResponse(product);
        return ResponseEntity.created(uri).body(dto);
    }

    public ResponseEntity<Page<ProductResponse>> getAllService(Pageable pagination) {
        Page<ProductResponse> page = repository.findAll(pagination).map(ProductResponse::new);
        return ResponseEntity.ok(page);
    }

    public ResponseEntity<ProductResponse> putProductService(UpdateProductRequest data) {
        Product product = repository.findById(data.id()).orElseThrow(
                () -> new EntityNotFoundException("Produto não encontrado com id: " + data.id()));

        product.update(data.description(), data.unit(), data.status());
        ProductResponse dto = new ProductResponse(product);
        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<Void> deleteLogicProductService(Long id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Produto não encontrado com id: " + id));

        product.deleteLogic();
        return ResponseEntity.noContent().build();
    }
}
