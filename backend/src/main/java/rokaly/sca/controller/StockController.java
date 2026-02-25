package rokaly.sca.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rokaly.sca.dto.StockEntryRequest;
import rokaly.sca.dto.StockEntryExitResponse;
import rokaly.sca.dto.StockExitRequest;
import rokaly.sca.service.StockService;

import java.util.List;

@RestController
@RequestMapping("/api/v3/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("entry")
    @Transactional
    public ResponseEntity<StockEntryExitResponse> createEntries(@RequestBody @Valid StockEntryRequest data, UriComponentsBuilder uriBuilder) {
        return stockService.createEntries(data, uriBuilder);
    }

    @PostMapping("exit")
    @Transactional
    public ResponseEntity<Void> createExit(@RequestBody @Valid List<StockExitRequest> data) {
        return stockService.createExit(data);
    }

//    @PostMapping("transfer")
//    @Transactional
//    public ResponseEntity<StockResponse> createTransfer(@RequestBody @Valid StockRequest data) {
//        return stockService.createRecord(data);
//    }
//
//    @PostMapping("adjustment")
//    @Transactional
//    public ResponseEntity<StockResponse> createAdjustment(@RequestBody @Valid StockRequest data) {
//        return stockService.createRecord(data);
//    }

    @GetMapping
    public ResponseEntity<Page<StockEntryExitResponse>> getAll(@PageableDefault(size = 10, page = 0, sort = {"id"}) Pageable pagination) {
        return stockService.getAll(pagination);
    }
}
