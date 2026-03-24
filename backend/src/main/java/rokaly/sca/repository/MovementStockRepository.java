package rokaly.sca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rokaly.sca.entity.MovementStock;

public interface MovementStockRepository extends JpaRepository<MovementStock, Long> {
}
