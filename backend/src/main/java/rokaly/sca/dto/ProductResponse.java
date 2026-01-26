package rokaly.sca.dto;

import rokaly.sca.entity.Product;
import rokaly.sca.utils.enums.StatusProduct;

public record ProductResponse(Long id, String code, String description, String unit, StatusProduct status) {
}
