package rokaly.sca.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rokaly.sca.dto.request.StockAdjustmentRequest;
import rokaly.sca.dto.request.StockEntryRequest;
import rokaly.sca.dto.response.StockResponse;
import rokaly.sca.service.StockService;

@RestController
@RequestMapping("/api/v3/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/entry")
    @Transactional
    public ResponseEntity<StockResponse> createEntries(@RequestBody @Valid StockEntryRequest data, UriComponentsBuilder uriBuilder) {
        return stockService.createEntries(data, uriBuilder);
    }

    @PutMapping("/adjustment")
    @Transactional
    public ResponseEntity<StockResponse> createAdjustment(@RequestBody @Valid StockAdjustmentRequest data) {
        return stockService.createAdjustment(data);
    }

    @GetMapping
    public ResponseEntity<Page<StockResponse>> getAll(@PageableDefault(size = 10, page = 0, sort = {"id"}) Pageable pagination) {
        return stockService.getAll(pagination);
    }
}
