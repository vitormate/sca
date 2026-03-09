package rokaly.sca.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PickingOrderAssignRequest(@NotBlank String separator) {
}
