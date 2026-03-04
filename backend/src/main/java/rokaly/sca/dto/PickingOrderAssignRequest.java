package rokaly.sca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PickingOrderAssignRequest(@NotBlank String separator) {
}
