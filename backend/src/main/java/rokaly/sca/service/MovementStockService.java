package rokaly.sca.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rokaly.sca.dto.response.MovementStockResponse;
import rokaly.sca.repository.MovementStockRepository;

@Service
public class MovementStockService {

    private final MovementStockRepository movementStockRepository;

    public MovementStockService(MovementStockRepository movementStockRepository) {
        this.movementStockRepository = movementStockRepository;
    }

    public ResponseEntity<Page<MovementStockResponse>> getAll(Pageable pagination) {
        Page<MovementStockResponse> movementStock = movementStockRepository.findAll(pagination).map(MovementStockResponse::new);
        return ResponseEntity.ok(movementStock);
    }
}
