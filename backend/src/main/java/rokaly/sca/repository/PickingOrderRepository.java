package rokaly.sca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rokaly.sca.entity.PickingOrder;

import java.util.Optional;

public interface PickingOrderRepository extends JpaRepository<PickingOrder, Long> {
}
