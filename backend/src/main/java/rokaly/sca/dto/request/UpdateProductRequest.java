package rokaly.sca.dto.request;

import jakarta.validation.constraints.NotNull;
import rokaly.sca.utils.enums.StatusProduct;

public record UpdateProductRequest(@NotNull Long id, String description, String unit, StatusProduct status) {
}
