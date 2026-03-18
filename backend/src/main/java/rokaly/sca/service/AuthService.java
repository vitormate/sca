package rokaly.sca.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rokaly.sca.dto.request.LoginRequest;
import rokaly.sca.dto.response.LoginResponse;
import rokaly.sca.entity.User;
import rokaly.sca.infra.security.TokenService;
import rokaly.sca.repository.UserRepository;

@Service
public class AuthService {

    private final AuthenticationManager manager;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager manager, UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.manager = manager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<LoginResponse> login(LoginRequest data) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        Authentication authentication = manager.authenticate(authToken);
        String token = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new LoginResponse(data.username(), token));
    }
}
