package rokaly.sca.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import rokaly.sca.dto.request.PositionsRequest;
import rokaly.sca.dto.response.PositionsResponse;
import rokaly.sca.dto.request.UpdatePositionRequest;
import rokaly.sca.entity.Position;
import rokaly.sca.repository.PositionRepository;

@Service
public class PositionService {

    private final PositionRepository repository;

    public PositionService(PositionRepository positionRepository) {
        this.repository = positionRepository;
    }

    public ResponseEntity<PositionsResponse> createPositionService(PositionsRequest data, UriComponentsBuilder uriBuilder) {
        Position position = new Position(data.code());
        repository.save(position);
        var uri = uriBuilder.path("/position/{id}").buildAndExpand(position.getId()).toUri();
        PositionsResponse dto = new PositionsResponse(position);
        return ResponseEntity.created(uri).body(dto);
    }

    public ResponseEntity<Page<PositionsResponse>> getAllService(Pageable pagination) {
        Page<PositionsResponse> page = repository.findAll(pagination).map(PositionsResponse::new);
        return ResponseEntity.ok(page);
    }

    public ResponseEntity<PositionsResponse> putPositionService(UpdatePositionRequest data) {
        Position position = repository.findById(data.id()).orElseThrow(
                () -> new EntityNotFoundException("Posição não encontrado com id: " + data.id()));
        position.update(data.code(), data.status());
        PositionsResponse dto = new PositionsResponse(position);
        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<Void> deleteLogicService(Long id) {
        Position position = repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Posição não encontrado com id: " + id));
        position.deleteLogic();
        return ResponseEntity.noContent().build();
    }
}
