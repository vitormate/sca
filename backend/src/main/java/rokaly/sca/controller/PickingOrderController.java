package rokaly.sca.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import rokaly.sca.dto.PickingOrderCreateRequest;
import rokaly.sca.service.PickingOrderService;

@RestController
@RequestMapping("/api/v3/order")
public class PickingOrderController {

    private final PickingOrderService pickingOrderService;

    public PickingOrderController(PickingOrderService pickingOrderService) {
        this.pickingOrderService = pickingOrderService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Void> createOrder(@RequestBody @Valid PickingOrderCreateRequest data, UriComponentsBuilder uriBuilder) {
        return pickingOrderService.createOrder(data, uriBuilder);
    }
}
