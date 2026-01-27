package rokaly.sca.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rokaly.sca.dto.PositionsRequest;
import rokaly.sca.dto.PositionsResponse;
import rokaly.sca.service.PositionsService;

@RestController
@RequestMapping("/api/v1/positions")
public class PositionsController {

    private final PositionsService positionsService;

    public PositionsController(PositionsService positionsService) {
        this.positionsService = positionsService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<PositionsResponse> createPosition(@RequestBody @Valid PositionsRequest data, UriComponentsBuilder uriBuilder) {
        return positionsService.createPositionService(data, uriBuilder);
    }

    @GetMapping
    public ResponseEntity<Page<PositionsResponse>> getAll(@PageableDefault(size = 10, page = 0, sort = {"id"}) Pageable pagination) {
        return positionsService.getAllService(pagination);
    }
}
