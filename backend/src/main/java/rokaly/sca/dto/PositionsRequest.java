package rokaly.sca.dto;

import jakarta.validation.constraints.NotBlank;

public record PositionsRequest(@NotBlank String code) {
}
