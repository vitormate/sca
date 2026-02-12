package rokaly.sca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record StockEntryRequest(@NotNull Long productId,
                           @NotNull Long positionId,
                           @NotNull BigDecimal amount,
                           @NotBlank String name,
                           String reason) {
}
