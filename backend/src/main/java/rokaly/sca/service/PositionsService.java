package rokaly.sca.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import rokaly.sca.dto.PositionsRequest;
import rokaly.sca.dto.PositionsResponse;
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

    public ResponseEntity<Page<PositionsResponse>> getAllService(Pageable pagination) {
        Page<PositionsResponse> page = repository.findAll(pagination).map(p -> new PositionsResponse(p.getId(), p.getCode(), p.getStatus().getStatusPT()));
        return ResponseEntity.ok(page);
    }
}
