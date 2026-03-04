package rokaly.sca.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PickingProductsRequest(
        @NotNull
        BigDecimal requestedAmount,
        @NotNull
        Long productId
) {
}
