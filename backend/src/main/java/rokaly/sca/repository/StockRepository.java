package rokaly.sca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rokaly.sca.entity.Stock;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("SELECT s FROM Stock s WHERE s.product.code = :code")
    List<Stock> findByProductCode(@Param("code") String productCode);

    @Query("SELECT s.position.code FROM Stock s WHERE s.product.id = :productId")
    List<String> findPositionByProductId(Long productId);

    @Query("SELECT s FROM Stock s WHERE s.position.code = :positionCode")
    Optional<Stock> findByPositionCode(String positionCode);
}
