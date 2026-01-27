package rokaly.sca.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import rokaly.sca.dto.PositionsRequest;
import rokaly.sca.dto.PositionsResponse;
import rokaly.sca.dto.UpdatePositionRequest;
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
        PositionsResponse dto = new PositionsResponse(positions);
        return ResponseEntity.created(uri).body(dto);
    }

    public ResponseEntity<Page<PositionsResponse>> getAllService(Pageable pagination) {
        Page<PositionsResponse> page = repository.findAll(pagination).map(PositionsResponse::new);
        return ResponseEntity.ok(page);
    }

    public ResponseEntity<PositionsResponse> putPositionService(UpdatePositionRequest data) {
        Positions positions = repository.findById(data.id()).orElseThrow(
                () -> new EntityNotFoundException("Posição não encontrado com id: " + data.id()));
        positions.update(data.code(), data.status());
        PositionsResponse dto = new PositionsResponse(positions);
        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<Void> deleteLogicService(Long id) {
        Positions positions = repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Posição não encontrado com id: " + id));
        positions.deleteLogic();
        return ResponseEntity.noContent().build();
    }
}
