package rokaly.sca.dto.response;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record CustomErrorResponse(LocalDateTime timestamp, int status, String error, String message) {
}