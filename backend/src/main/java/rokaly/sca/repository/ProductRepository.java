package rokaly.sca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rokaly.sca.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
