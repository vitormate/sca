package rokaly.sca.dto.response;

import rokaly.sca.entity.PickingProduct;
import rokaly.sca.utils.enums.PickingProductCollectedStatus;
import rokaly.sca.utils.enums.PickingProductStatus;

import java.math.BigDecimal;

public record PickingProductsResponse(
        Long id,
        BigDecimal requestedAmount,
        BigDecimal collectedAmount,
        String productCode,
        String productName,
        String suggestedPosition,
        PickingProductStatus status,
        PickingProductCollectedStatus collectedStatus
        ) {
    public PickingProductsResponse(PickingProduct p) {
        this(
                p.getId(),
                p.getRequestedAmount(),
                p.getCollectedAmount(),
                p.getProduct().getCode(),
                p.getProduct().getName(),
                p.getSuggestedPosition(),
                p.getStatus(),
                p.getStatusCollected()
        );
    }
}
