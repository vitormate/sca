package rokaly.sca.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PickingProductsRequest(
        @NotNull
        BigDecimal requestedAmount,
        @NotNull
        Long productId
) {
}
