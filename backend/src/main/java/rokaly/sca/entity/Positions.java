package rokaly.sca.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rokaly.sca.utils.enums.StatusPosition;

@Getter
@Setter
@NoArgsConstructor
public class Positions {
    private String code;
    private String description;
    private StatusPosition status;
}
