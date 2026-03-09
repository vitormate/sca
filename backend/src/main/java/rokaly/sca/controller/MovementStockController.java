package rokaly.sca.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rokaly.sca.dto.response.MovementStockResponse;
import rokaly.sca.service.MovementStockService;

@RestController
@RequestMapping("/api/v4/movement")
public class MovementStockController {

    private final MovementStockService movementStockService;

    public MovementStockController(MovementStockService movementStockService) {
        this.movementStockService = movementStockService;
    }

    @GetMapping
    public ResponseEntity<Page<MovementStockResponse>> getAll(@PageableDefault(size = 10, page = 0, sort = {"id"}) Pageable pagination) {
        return movementStockService.getAll(pagination);
    }
}
