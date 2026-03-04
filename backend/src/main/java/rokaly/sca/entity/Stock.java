package rokaly.sca.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
//            Substitutir por business exception quando fiz a global exception
            throw new RuntimeException("Amount need to be bigger than 0(zero). Amount: " + amount);
        }

    }

    public static void hasStock(List<Stock> listStock, String productCode, BigDecimal amount) {
        BigDecimal checkAmount = listStock.stream()
                .map(Stock::getAmount)
                .reduce(BigDecimal.ZERO, (BigDecimal::add));

        if (checkAmount.compareTo(amount) < 0) {
//            Substitutir por business exception quando fiz a global exception
            throw new RuntimeException("Insufficient Stock Amount! Product: " + productCode + " | Amount: " + checkAmount);
        }
    }

    public void validStock() {
        if (this.amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Insufficient stock! Stock: " + this.amount);
        }
    }

    public void updateAmount(BigDecimal collectedAmount) {
        this.setAmount(this.getAmount().subtract(collectedAmount));
    }

    public void addAmount(BigDecimal collectedAmount) {
        this.setAmount(this.amount.add(collectedAmount));
    }


}
