package rokaly.sca.dto;

import rokaly.sca.utils.enums.StatusProduct;

public record ProductResponse(Long id, String code, String description, String unit, StatusProduct status) {
    public ProductResponse(Long id, ProductResquest data, StatusProduct status) {
        this(id, data.code(), data.description(), data.unit(), status);
    }
}
