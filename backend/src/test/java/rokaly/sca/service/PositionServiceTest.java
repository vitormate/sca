package rokaly.sca.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import rokaly.sca.dto.request.UpdatePositionRequest;
import rokaly.sca.entity.Position;
import rokaly.sca.repository.PositionRepository;
import rokaly.sca.utils.enums.StatusPosition;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PositionServiceTest {

    @Mock
    PositionRepository positionRepository;

    @InjectMocks
    PositionService positionService;

    UpdatePositionRequest update;
    Position position;

    @BeforeEach
    void setUp() {
        this.update = new UpdatePositionRequest(1L, null, null);

        this.position = new Position("AP01-01-02");
    }

    @Nested
    class putPositionTests {

        @Test
        void shouldReturnStatusCode200() {

            when(positionRepository.findById(1L)).thenReturn(Optional.of(position));

            int statusCode = positionService.putPositionService(update).getStatusCode().value();

            assertEquals(HttpStatus.OK.value(), statusCode);
        }

        @Test
        void shouldReturnEntityNotFoundWhenProductNotFound() {
            when(positionRepository.findById(1L)).thenReturn(Optional.empty());

            final EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> positionService.putPositionService(update)
            );

            assertEquals("Position not found with id: " + update.id(), exception.getMessage());
        }
    }

    @Nested
    class deleteLogicTests {

        @Test
        void shouldReturnStatusCode204() {
            when(positionRepository.findById(1L)).thenReturn(Optional.of(position));

            int statusCode = positionService.deleteLogicService(1L).getStatusCode().value();

            assertEquals(HttpStatus.NO_CONTENT.value(), statusCode);
        }

        @Test
        void shouldReturnEntityNotFoundWhenProductNotFound() {
            when(positionRepository.findById(1L)).thenReturn(Optional.empty());

            final EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> positionService.deleteLogicService(1L)
            );

            assertEquals("Position not found with id: 1", exception.getMessage());
        }
    }
}