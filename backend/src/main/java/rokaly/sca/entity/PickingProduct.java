package rokaly.sca.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rokaly.sca.utils.enums.PickingProductCollectedStatus;
import rokaly.sca.utils.enums.PickingProductStatus;

import java.math.BigDecimal;

@Entity
@Table(name = "picking_products")
@Getter
@Setter
@NoArgsConstructor
public class PickingProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requested_amount", nullable = false)
    private BigDecimal requestedAmount;

    @Column(name = "collected_amount", nullable = false)
    private BigDecimal collectedAmount;

    @Column(name = "suggested_position", length = 20, nullable = false)
    private String suggestedPosition;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private PickingOrder pickingOrder;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private PickingProductStatus status;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private PickingProductCollectedStatus statusCollected;

    public PickingProduct(BigDecimal requestedAmount, String suggestedPosition, Product product, PickingOrder pickingOrder) {
        this.requestedAmount = requestedAmount;
        this.collectedAmount = BigDecimal.ZERO;
        this.suggestedPosition = suggestedPosition;
        this.product = product;
        this.pickingOrder = pickingOrder;
        this.status = PickingProductStatus.WAITING;
        this.statusCollected = PickingProductCollectedStatus.WAITING;
    }
}
