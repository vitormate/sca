package rokaly.sca.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PickingOrderCreateRequest(
        @NotBlank
        String createdBy,
        @NotNull
        List<PickingProductsRequest> pickingProducts
) {
}
