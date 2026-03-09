package rokaly.sca.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PickingOrderCancelRequest(
        @NotBlank String name,
        String reason
) {
}
