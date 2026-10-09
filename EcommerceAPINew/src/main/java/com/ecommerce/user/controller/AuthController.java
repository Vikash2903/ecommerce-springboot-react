package com.ecommerce.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.*;

import com.ecommerce.exception.ApiResponse;
import com.ecommerce.security.JwtService;
import com.ecommerce.user.dto.request.UserRequest;
import com.ecommerce.user.dto.response.UserResponse;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.dto.request.LoginRequest;
import com.ecommerce.user.dto.response.LoginResponse;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.service.UserService;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication API",description = "Registration and login APIs")
public class AuthController 
{
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService) 
    {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new customer")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody UserRequest request) 
    {
        UserResponse response = userService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "User registered successfully",
                                response
                        )
                );
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates user and returns JWT token")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) 
    {
        String email = request.getEmail().trim().toLowerCase();
        log.info("Login attempt for email: {}", email);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));
        
        User user = userRepository.findByEmailIgnoreCase(email).orElseThrow();
        
        String token = jwtService.generateToken(user);

        log.info("Login successful for user id: {}", user.getId());

        LoginResponse response = new LoginResponse(token, "Bearer", user.getId(), user.getName(), user.getEmail(), user.getRole().name());
        
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Login successful",
                        response
                )
        );
    }
}