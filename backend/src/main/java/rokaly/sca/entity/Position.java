package rokaly.sca.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rokaly.sca.exception.BusinessException;
import rokaly.sca.utils.enums.StatusPosition;

@Entity
@Table(name = "positions")
@Getter
@Setter
@NoArgsConstructor
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String code;

    @Column(length = 25, nullable = false)
    private StatusPosition status;

    public Position(String code) {
        this.code = code;
        this.status = StatusPosition.ACTIVE;
    }

    public void update(String code, StatusPosition status) {
        if (code != null) this.code = code;
        if (status != null) this.status = status;
    }

    public void deleteLogic() {
        this.status = StatusPosition.INACTIVE;
    }

    public void isActivePositionStatus(StatusPosition status) {
        if (status != StatusPosition.ACTIVE) {
            throw new BusinessException("Position not active. Status: " + status);
        }
    }
}
