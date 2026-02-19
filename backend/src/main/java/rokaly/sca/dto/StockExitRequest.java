package rokaly.sca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record StockExitRequest(@NotBlank String productCode, @NotNull BigDecimal amount) {
}
