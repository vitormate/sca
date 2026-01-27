package rokaly.sca.dto;

import rokaly.sca.entity.Position;

public record PositionsResponse(Long id, String code, String status) {
    public PositionsResponse(Position p) {
        this(p.getId(), p.getCode(), p.getStatus().getStatusPT());
    }
}
