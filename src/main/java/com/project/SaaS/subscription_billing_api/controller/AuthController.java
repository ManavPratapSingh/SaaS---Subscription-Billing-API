package com.project.SaaS.subscription_billing_api.controller;

import com.project.SaaS.subscription_billing_api.annotation.RateLimit;
import com.project.SaaS.subscription_billing_api.dto.AuthResponse;
import com.project.SaaS.subscription_billing_api.dto.LoginRequest;
import com.project.SaaS.subscription_billing_api.dto.RegisterRequest;
import com.project.SaaS.subscription_billing_api.entity.User;
import com.project.SaaS.subscription_billing_api.security.JwtUtil;
import com.project.SaaS.subscription_billing_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and user registration endpoints")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    @RateLimit
    @Operation(summary = "Register a new user", description = "Creates a new user account with the provided credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data or username/email already exists", content = @Content),
            @ApiResponse(responseCode = "429", description = "Too many requests - rate limit exceeded", content = @Content)
    })
    public ResponseEntity<?> registerUser(
            @Parameter(description = "User registration details", required = true) @Valid @RequestBody RegisterRequest registerRequest) {
        User user = userService.registerUser(registerRequest);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("username", user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @RateLimit
    @Operation(summary = "Login user", description = "Authenticates user credentials and returns a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content),
            @ApiResponse(responseCode = "429", description = "Too many requests - rate limit exceeded", content = @Content)
    })
    public ResponseEntity<?> loginUser(
            @Parameter(description = "User login credentials", required = true) @Valid @RequestBody LoginRequest loginRequest) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        // Get user details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generate JWT token
        String token = jwtUtil.generateToken(userDetails);

        // Fetch full user entity from database to get email
        User user = userService.getUserByUsername(userDetails.getUsername());

        // Build response
        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return ResponseEntity.ok(authResponse);
    }
}
