package com.pcshop.userservice.controller;

import com.pcshop.userservice.dto.LoginRequest;
import com.pcshop.userservice.dto.SignupRequest;
import com.pcshop.userservice.security.JwtUtil;
import com.pcshop.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest signupRequest) {
        if (userService.userExists(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // Determine roles (default to BUYER)
        Set<String> roles = signupRequest.getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = Set.of("BUYER");
        }

        userService.saveUser(signupRequest.getUsername(),
                signupRequest.getPassword(),
                signupRequest.getDeliveryAddress(),
                roles);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());

        if (userDetails == null || !passwordEncoder.matches(
                loginRequest.getPassword(), userDetails.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        List<String> roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());

        String token = jwtUtil.generateToken(userDetails.getUsername(), roles);
        return ResponseEntity.ok(token);
    }

}
