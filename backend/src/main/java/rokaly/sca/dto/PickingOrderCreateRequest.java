package rokaly.sca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record PickingOrderCreateRequest(
        @NotBlank
        String createdBy,
        @NotNull
        List<PickingProductsRequest> pickingProducts
) {
}
