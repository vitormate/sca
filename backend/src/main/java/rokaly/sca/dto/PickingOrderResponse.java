package rokaly.sca.dto;

import rokaly.sca.entity.PickingOrder;
import rokaly.sca.utils.enums.PickingOrderStatus;

import java.time.LocalDateTime;

public record PickingOrderResponse(
        Long id,
        String createdBy,
        LocalDateTime createdAt,
        String separator,
        LocalDateTime assignedAt,
        LocalDateTime finishedAt,
        LocalDateTime canceledAt,
        PickingOrderStatus status
) {
    public PickingOrderResponse(PickingOrder o) {
        this(
                o.getId(),
                o.getCreatedBy(),
                o.getCreatedAt(),
                o.getSeparator() != null ? o.getSeparator().getUsername() : null,
                o.getAssignedAt(),
                o.getFinishedAt(),
                o.getCanceledAt(),
                o.getStatus());
    }
}
