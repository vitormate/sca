package rokaly.sca.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import rokaly.sca.utils.enums.Role;

public record RegisterRequest(@NotBlank String username, @NotBlank String password, @NotNull Role role) {
}
