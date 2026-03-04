package rokaly.sca.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rokaly.sca.utils.enums.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movement")
@Getter
@Setter
@NoArgsConstructor
public class MovementStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_code", length = 50, nullable = false)
    private String productCode;

    @Column(name = "product_name", length = 150)
    private String productName;

    @Column(name = "from_position", length = 50)
    private String fromPosition;

    @Column(name = "to_position", length = 50)
    private String toPosition;

    @Column(name = "requested_by", length = 50)
    private String requestedBy;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", length = 25, nullable = false)
    private MovementType movementType;

    @Column(length = 100, nullable = false)
    private String responsible;

    @Column(length = 255)
    private String reason;

    public static MovementStock createIn(String productCode, String productName, String positionCode, BigDecimal amount, String name, String reason) {
        MovementStock movementStock = new MovementStock();
        movementStock.productCode = productCode;
        movementStock.productName = productName;
        movementStock.toPosition = positionCode;
        movementStock.requestedBy = name;
        movementStock.amount = amount;
        movementStock.dateTime = LocalDateTime.now();
        movementStock.movementType = MovementType.IN;
        movementStock.responsible = name;
        movementStock.reason = reason;
        return movementStock;
    }

    public static MovementStock createOut(String productCode, String productName, String positionCode, BigDecimal amount, String name) {
        MovementStock movementStock = new MovementStock();
        movementStock.productCode = productCode;
        movementStock.productName = productName;
        movementStock.fromPosition = positionCode;
        movementStock.requestedBy = name;
        movementStock.amount = amount;
        movementStock.dateTime = LocalDateTime.now();
        movementStock.movementType = MovementType.OUT;
        movementStock.responsible = name;
        movementStock.reason = "";
        return movementStock;
    }
}
