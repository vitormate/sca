package rokaly.sca.dto.response;

import rokaly.sca.entity.MovementStock;
import rokaly.sca.utils.enums.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovementStockResponse(
        Long id,
        String productCode,
        String productName,
        String fromPosition,
        String toPosition,
        String requestedBy,
        BigDecimal amount,
        LocalDateTime dateTime,
        String movementType,
        String responsible,
        String reason
) {

    public MovementStockResponse(MovementStock m) {
        this(
                m.getId(),
                m.getProductCode(),
                m.getProductName(),
                m.getFromPosition(),
                m.getToPosition(),
                m.getRequestedBy(),
                m.getAmount(),
                m.getDateTime(),
                m.getMovementType().getType(),
                m.getResponsible(),
                m.getReason()
        );
    }
}
