package rokaly.sca.dto;

import jakarta.validation.constraints.NotNull;
import rokaly.sca.utils.enums.StatusPosition;

public record UpdatePositionRequest(@NotNull Long id, String code, StatusPosition status) {
}
