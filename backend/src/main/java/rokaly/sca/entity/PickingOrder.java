package rokaly.sca.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rokaly.sca.dto.request.PickingProductsRequest;
import rokaly.sca.exception.BusinessException;
import rokaly.sca.utils.enums.PickingOrderStatus;
import rokaly.sca.utils.enums.PickingProductCollectedStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "picking_order")
@Getter
@Setter
@NoArgsConstructor
public class PickingOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "separator_id")
    private User separator;

    @Column(name = "assigned_at", nullable = true)
    private LocalDateTime assignedAt;

    @Column(name = "finished_at", nullable = true)
    private LocalDateTime finishedAt;

    @Column(name = "canceled_at", nullable = true)
    private LocalDateTime canceledAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PickingOrderStatus status;

    @OneToMany(mappedBy = "pickingOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PickingProduct> pickingProducts = new ArrayList<>();

    public PickingOrder(String createdBy, LocalDateTime createdAt, PickingOrderStatus status) {
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.status = status;
    }

    public static void validProducts(List<PickingProductsRequest> pickingProducts) {

        boolean hasDuplicates = pickingProducts.stream()
                .map(PickingProductsRequest::productId)
                .distinct()
                .count() != pickingProducts.size();

        if (hasDuplicates) {
            throw new BusinessException("The products must be different in a Picking Order!");
        }

    }

    public void checkAndFinish() {
        boolean check = this.getPickingProducts().stream()
                .allMatch(p -> p.getStatusCollected() == PickingProductCollectedStatus.COMPLETED);

        if (check) {
            this.setStatus(PickingOrderStatus.FINISHED);
            this.setFinishedAt(LocalDateTime.now());
        }
    }
}
