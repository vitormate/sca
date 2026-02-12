package rokaly.sca.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "stock", uniqueConstraints = {@UniqueConstraint(columnNames = {"product_id", "position_id"})})
@Getter
@Setter
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "position_id")
    private Position position;

    @Column(nullable = false)
    private BigDecimal amount;

    public Stock(Product product, Position position, BigDecimal amount) {
        this.product = product;
        this.position = position;
        this.amount = amount;
    }


    public static void isValidEntryAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount precisa ser maior do que 0(zero)");
        }

    }
}
