package rokaly.sca.dto.response;

import rokaly.sca.entity.Product;

public record ProductResponse(Long id, String code, String description, String unit, String status) {
    public ProductResponse(Product p) {
        this(p.getId(), p.getCode(), p.getName(), p.getUnit(), p.getStatus().getStatusPT());
    }
}
