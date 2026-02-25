package rokaly.sca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rokaly.sca.entity.PickingOrder;

public interface PickingOrderRepository extends JpaRepository<PickingOrder, Long> {
}
