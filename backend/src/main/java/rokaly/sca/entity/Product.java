package rokaly.sca.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Column(length = 50, nullable = false, unique = true)
    private String description;

    @Column(length = 10, nullable = false)
    private String unit;

    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusProduct status;

    public Product(String code, String description, String unit) {
        this.code = code;
        this.description = description;
        this.unit = unit;
        this.status = StatusProduct.ACTIVE;
    }
}
