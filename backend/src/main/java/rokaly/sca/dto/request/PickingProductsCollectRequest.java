package rokaly.sca.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PickingProductsCollectRequest(@NotBlank String positionCode) {
}
