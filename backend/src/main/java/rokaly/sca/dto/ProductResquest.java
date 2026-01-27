package rokaly.sca.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductResquest(
        @NotBlank
        String code,
        @NotBlank
        String description,
        @NotBlank
        String unit) {
}
