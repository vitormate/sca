package rokaly.sca.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rokaly.sca.utils.enums.StatusProduct;

@Getter
@Setter
@NoArgsConstructor
public class Product {
    private String code;
    private String description;
    private String unit;
    private StatusProduct status;
}
