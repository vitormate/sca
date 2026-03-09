package rokaly.sca.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record StockAdjustmentRequest(
        @NotNull Long id,
        @NotNull BigDecimal newAmount,
        @NotBlank String responsible,
        @NotBlank String reason
        ) {
}
