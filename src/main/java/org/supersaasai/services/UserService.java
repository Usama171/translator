package org.supersaasai.services;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.supersaasai.configs.JwtUtil;
import org.supersaasai.entities.User;
import org.supersaasai.repository.UserRepository;
import org.supersaasai.requests.LoginRequest;
import org.supersaasai.requests.RegisterRequest;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService  {

    private final   UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new  User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        String token = jwtUtil.generateToken(user);

        return token;
    }
    public String login(LoginRequest req) {
        Optional<User> optionalUser = userRepository.findByEmail(req.getEmail());

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + req.getEmail());
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect password for user: " + req.getEmail());
        }

        return jwtUtil.generateToken(user);
    }



    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
