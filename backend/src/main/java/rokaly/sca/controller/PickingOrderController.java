package rokaly.sca.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rokaly.sca.dto.request.PickingOrderAssignRequest;
import rokaly.sca.dto.request.PickingOrderCreateRequest;
import rokaly.sca.dto.request.PickingProductsCollectRequest;
import rokaly.sca.dto.response.PickingOrderResponse;
import rokaly.sca.dto.response.PickingProductsResponse;
import rokaly.sca.service.PickingOrderService;

import java.util.List;

@RestController
@RequestMapping("/api/v4/order")
public class PickingOrderController {

    private final PickingOrderService pickingOrderService;

    public PickingOrderController(PickingOrderService pickingOrderService) {
        this.pickingOrderService = pickingOrderService;
    }

    @PostMapping("new")
    @Transactional
    public ResponseEntity<Void> createOrder(@RequestBody @Valid PickingOrderCreateRequest data) {
        return pickingOrderService.createOrder(data);
    }

    @GetMapping
    public ResponseEntity<Page<PickingOrderResponse>> getAll(@PageableDefault(size = 10, page = 0, sort = {"id"}) Pageable pagination) {
        return pickingOrderService.getAll(pagination);
    }

    @GetMapping("/{orderId}/products")
    public ResponseEntity<List<PickingProductsResponse>> getAllProductsFromOrder(@PathVariable Long orderId) {
        return pickingOrderService.getAllProductsFromOrder(orderId);
    }

    @PutMapping("/{orderId}/products/assign")
    @Transactional
    public ResponseEntity<Void> assignOrder(@PathVariable Long orderId, @RequestBody @Valid PickingOrderAssignRequest data) {
        return pickingOrderService.assignOrder(orderId, data);
    }


    @PutMapping("/{orderId}/products/{pickingProductId}/collect")
    @Transactional
    public ResponseEntity<PickingProductsResponse> collectProduct(@PathVariable Long orderId, @PathVariable Long pickingProductId, @RequestBody @Valid PickingProductsCollectRequest data) {
        return pickingOrderService.collectProduct(orderId, pickingProductId, data);
    }

    @PutMapping("/{orderId}/products/finish")
    @Transactional
    public ResponseEntity<PickingOrderResponse> finishOrderWithPartialCollection(@PathVariable Long orderId) {
        return pickingOrderService.finishOrderWithPartialCollection(orderId);
    }
}
