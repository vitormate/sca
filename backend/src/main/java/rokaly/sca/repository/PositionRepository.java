package rokaly.sca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rokaly.sca.entity.Positions;

public interface PositionRepository extends JpaRepository<Positions, Long> {
}
