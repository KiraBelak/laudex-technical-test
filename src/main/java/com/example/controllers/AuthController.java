package com.example.controllers;

import com.example.dto.JwtResponseDTO;
import com.example.dto.LoginRequestDTO;
import com.example.dto.RegisterRequestDTO;
import com.example.models.User;
import com.example.security.JwtUtils;
import com.example.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authentication
                .getPrincipal();

        return ResponseEntity.ok(new JwtResponseDTO(
                jwt,
                null, // ID no disponible en UserDetails
                userDetails.getUsername(),
                null, // Email no disponible en UserDetails
                userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(java.util.stream.Collectors.toSet())));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        if (userService.existsByUsername(registerRequest.getUsername())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error: El nombre de usuario ya está en uso");
            return ResponseEntity.badRequest().body(response);
        }

        if (userService.existsByEmail(registerRequest.getEmail())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error: El email ya está en uso");
            return ResponseEntity.badRequest().body(response);
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setFullName(registerRequest.getFullName());
        user.setRoles(Collections.singleton("ROLE_USER"));

        userService.createUser(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuario registrado exitosamente");
        return ResponseEntity.ok(response);
    }
}