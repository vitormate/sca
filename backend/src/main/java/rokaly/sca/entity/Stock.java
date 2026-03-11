package rokaly.sca.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rokaly.sca.exception.BusinessException;

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


    public static void isValidAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Amount need to be bigger than 0(zero). Amount: " + amount);
        }

    }

    public void validStock() {
        if (this.amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Insufficient stock! Stock: " + this.amount);
        }
    }

    public void collectAmount(BigDecimal collectedAmount) {
        this.setAmount(this.getAmount().subtract(collectedAmount));
    }

    public void addAmount(BigDecimal collectedAmount) {
        this.setAmount(this.amount.add(collectedAmount));
    }


    public void updateAmount(BigDecimal newAmount) {
        this.setAmount(newAmount);
    }
}
