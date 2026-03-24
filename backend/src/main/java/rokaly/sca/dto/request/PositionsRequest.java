package rokaly.sca.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PositionsRequest(@NotBlank String code) {
}
