package rokaly.sca.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rokaly.sca.dto.*;
import rokaly.sca.service.PositionService;

@RestController
@RequestMapping("/api/v2/positions")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<PositionsResponse> createPosition(@RequestBody @Valid PositionsRequest data, UriComponentsBuilder uriBuilder) {
        return positionService.createPositionService(data, uriBuilder);
    }

    @GetMapping
    public ResponseEntity<Page<PositionsResponse>> getAll(@PageableDefault(size = 10, page = 0, sort = {"id"}) Pageable pagination) {
        return positionService.getAllService(pagination);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<PositionsResponse> putPosition(@RequestBody @Valid UpdatePositionRequest data) {
        return positionService.putPositionService(data);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteLogicPosition(@PathVariable Long id) {
        return positionService.deleteLogicService(id);
    }
}
