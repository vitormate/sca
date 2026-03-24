package rokaly.sca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rokaly.sca.entity.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {
}
