package com.JFBRA.controller;

import com.JFBRA.dto.LoginRequest;
import com.JFBRA.dto.RegisterRequest;
import com.JFBRA.model.User;
import com.JFBRA.repository.UserRepository;
import com.JFBRA.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000; // 24h

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles("USER");
        user.setCreatedAt(LocalDateTime.now());
        user.setLastLoginAt(LocalDateTime.now());
        user.setIsLoggedIn(true);

        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getEmail(), "USER");
        long expiresAtMillis = System.currentTimeMillis() + EXPIRATION_MS;

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "expiresAtMillis", expiresAtMillis
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body("User not found");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        user.setIsLoggedIn(true);
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        String role = user.getRolesAsSet().stream().findFirst().orElse("USER");

        String token = jwtUtils.generateToken(user.getEmail(), role);
        long expiresAtMillis = System.currentTimeMillis() + EXPIRATION_MS;

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "expiresAtMillis", expiresAtMillis
                )
        );
    }
}
