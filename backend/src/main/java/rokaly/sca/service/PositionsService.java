package rokaly.sca.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import rokaly.sca.dto.PositionsRequest;
import rokaly.sca.dto.PositionsResponse;
import rokaly.sca.dto.ProductResponse;
import rokaly.sca.entity.Positions;
import rokaly.sca.repository.PositionRepository;

@Service
public class PositionsService {

    private final PositionRepository repository;

    public PositionsService(PositionRepository positionRepository) {
        this.repository = positionRepository;
    }

    public ResponseEntity<PositionsResponse> createPositionService(PositionsRequest data, UriComponentsBuilder uriBuilder) {
        Positions positions = new Positions(data.code());
        repository.save(positions);
        var uri = uriBuilder.path("/positions/{id}").buildAndExpand(positions.getId()).toUri();
        PositionsResponse dto = new PositionsResponse(positions.getId(), data.code(), positions.getStatus().getStatusPT());
        return ResponseEntity.created(uri).body(dto);
    }
}
