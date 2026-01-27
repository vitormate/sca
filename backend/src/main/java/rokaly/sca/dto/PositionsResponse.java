package rokaly.sca.dto;

import rokaly.sca.entity.Positions;

public record PositionsResponse(Long id, String code, String status) {
    public PositionsResponse(Positions p) {
        this(p.getId(), p.getCode(), p.getStatus().getStatusPT());
    }
}
