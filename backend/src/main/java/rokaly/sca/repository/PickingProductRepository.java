package rokaly.sca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rokaly.sca.entity.PickingProduct;

public interface PickingProductRepository extends JpaRepository<PickingProduct, Long> {
}
