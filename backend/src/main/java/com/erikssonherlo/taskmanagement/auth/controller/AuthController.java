package com.erikssonherlo.taskmanagement.auth.controller;

import com.erikssonherlo.taskmanagement.auth.dto.LoginDTO;
import com.erikssonherlo.taskmanagement.auth.dto.RegisterDTO;
import com.erikssonherlo.taskmanagement.auth.service.AuthService;
import com.erikssonherlo.taskmanagement.common.payload.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Authentication", description = "Endpoints for user login and registration")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Authenticate a user and return a JWT token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful login",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials")
            }
    )
    public AuthResponse login(@RequestBody @Valid LoginDTO request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register",
            description = "Register a new user and return a JWT token.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User registered successfully",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "400", description = "User already exists or invalid input")
            }
    )
    public AuthResponse register(@RequestBody @Valid RegisterDTO request) {
        return authService.register(request);
    }
}
