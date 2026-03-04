package rokaly.sca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rokaly.sca.entity.PickingProduct;

import java.util.List;
import java.util.Optional;

public interface PickingProductRepository extends JpaRepository<PickingProduct, Long> {
    @Query("SELECT pp FROM PickingProduct pp WHERE pp.id = :id AND pp.pickingOrder.id = :orderId")
    Optional<PickingProduct> findByIdAndOrderId(@Param("id") Long pickingProductId, Long orderId);
}
