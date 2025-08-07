package org.supersaasai.controllers;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supersaasai.requests.LoginRequest;
import org.supersaasai.requests.RegisterRequest;
import org.supersaasai.requests.AuthResponse;
import org.supersaasai.services.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        String token = service.register(req);
        return ResponseEntity.ok(new AuthResponse("User registered successfully", token));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        String token = service.login(req);
        return ResponseEntity.ok(new AuthResponse("Login successful", token));
    }

}
