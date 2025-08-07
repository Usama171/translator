package org.supersaasai.services;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.supersaasai.configs.JwtUtil;
import org.supersaasai.requests.LoginRequest;
import org.supersaasai.requests.RegisterRequest;
import org.supersaasai.entities.User;
import org.supersaasai.repository.UserRepository;
import org.supersaasai.requests.AuthResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered.");
        }

        User user=new User();
        user.setName(request.getName());
        user.setEmail(request.getName());
        user.setPassword(request.getPassword());

        userRepository.save(user);

        String token = jwtUtil.generateToken(user);

        return new AuthResponse("User registered successfully", token);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);

        return new AuthResponse("Login successful", token);
    }
}
