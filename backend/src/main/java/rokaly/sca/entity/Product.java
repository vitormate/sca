package rokaly.sca.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rokaly.sca.exception.BusinessException;
import rokaly.sca.utils.enums.StatusProduct;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String code;

    @Column(length = 150, nullable = false, unique = true)
    private String name;

    @Column(length = 10, nullable = false)
    private String unit;

    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusProduct status;

    public Product(String code, String name, String unit) {
        this.code = code;
        this.name = name;
        this.unit = unit;
        this.status = StatusProduct.ACTIVE;
    }

    public void update(String description, String unit, StatusProduct status) {
        if (description != null) this.name = description;
        if (unit != null) this.unit = unit;
        if (status != null) this.status = status;
    }

    public void deleteLogic() {
        this.status = StatusProduct.INACTIVE;
    }

    public void isActiveProductStatus(StatusProduct status) {
        if (status != StatusProduct.ACTIVE) {
            throw new BusinessException("Product is not active. Status: " + status);
        }
    }
}
