package rokaly.sca.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rokaly.sca.utils.enums.Role;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private String username;
    private String password;
    private Role role;
}
