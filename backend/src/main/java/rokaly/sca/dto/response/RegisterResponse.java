package rokaly.sca.dto.response;

import rokaly.sca.utils.enums.Role;

public record RegisterResponse(String username, Role role) {
}
