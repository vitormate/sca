package rokaly.sca.dto;

import jakarta.validation.constraints.NotBlank;

public record PickingProductsCollectRequest(@NotBlank String positionCode) {
}
