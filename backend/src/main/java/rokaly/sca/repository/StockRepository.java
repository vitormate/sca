package rokaly.sca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rokaly.sca.entity.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
