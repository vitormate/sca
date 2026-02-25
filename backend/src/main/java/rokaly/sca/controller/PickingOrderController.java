package rokaly.sca.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rokaly.sca.dto.PickingOrderCreateRequest;
import rokaly.sca.dto.PickingOrderResponse;
import rokaly.sca.service.PickingOrderService;

@RestController
@RequestMapping("/api/v3/order")
public class PickingOrderController {

    private final PickingOrderService pickingOrderService;

    public PickingOrderController(PickingOrderService pickingOrderService) {
        this.pickingOrderService = pickingOrderService;
    }

    @PostMapping("new")
    @Transactional
    public ResponseEntity<Void> createOrder(@RequestBody @Valid PickingOrderCreateRequest data, UriComponentsBuilder uriBuilder) {
        return pickingOrderService.createOrder(data, uriBuilder);
    }

    @GetMapping
    public ResponseEntity<Page<PickingOrderResponse>> getAll(@PageableDefault(size = 10, page = 0, sort = {"id"}) Pageable pagination) {
        return pickingOrderService.getAll(pagination);
    }
}
