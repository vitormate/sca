package rokaly.sca.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import rokaly.sca.dto.StockEntryRequest;
import rokaly.sca.dto.StockEntryResponse;
import rokaly.sca.service.StockService;

@RestController
@RequestMapping("/api/v2/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("entry")
    @Transactional
    public ResponseEntity<StockEntryResponse> createEntries(@RequestBody @Valid StockEntryRequest data, UriComponentsBuilder uriBuilder) {
        return stockService.createEntries(data, uriBuilder);
    }
//
//    @PostMapping("exit")
//    @Transactional
//    public ResponseEntity<StockResponse> createExit(@RequestBody @Valid StockRequest data) {
//        return stockService.createRecord(data);
//    }
//
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
}
