package rokaly.sca.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rokaly.sca.dto.request.LoginRequest;
import rokaly.sca.dto.request.RegisterRequest;
import rokaly.sca.dto.response.LoginResponse;
import rokaly.sca.dto.response.RegisterResponse;
import rokaly.sca.service.AuthService;

@RestController
@RequestMapping("/api/v4/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest data) {
        return authService.login(data);
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest data) throws BadRequestException {
        return authService.register(data);
    }
}
