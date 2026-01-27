package rokaly.sca.dto;

import rokaly.sca.entity.Product;
import rokaly.sca.utils.enums.StatusProduct;

public record ProductResponse(Long id, String code, String description, String unit, String status) {
    public ProductResponse(Product p) {
        this(p.getId(), p.getCode(), p.getDescription(), p.getUnit(), p.getStatus().getStatusPT());
    }
}
