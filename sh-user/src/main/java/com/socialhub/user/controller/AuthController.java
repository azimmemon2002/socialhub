
package com.socialhub.user.controller;

import com.socialhub.user.exception.CustomException;
import com.socialhub.user.dto.*;
import com.socialhub.user.service.UserService;
import com.socialhub.user.client.AuthServiceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * Controller to handle authentication-related endpoints.
 * Acts as a proxy between client applications and AUTH_SERVICE.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Endpoints for user authentication and registration")
public class AuthController {

    private final AuthServiceClient authServiceClient;
    private final UserService userService;

    /**
     * User Registration Endpoint
     * URL: POST /auth/register
     *
     * @param registerRequest containing user registration details
     * @return ResponseEntity with success message
     */
    @Operation(
            summary = "User Registration",
            description = "Register a new user with username, email, and password.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "201",
                            description = "User registered successfully"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data"
                    )
            }
    )
    @SecurityRequirement(name = "")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            var authResponse = authServiceClient.register(registerRequest);

            userService.createUserProfile(authResponse);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.builder()
                            .message("User registered successfully")
                            .status(HttpStatus.CREATED.value())
                            .build());
        } catch (HttpStatusCodeException ex) {
            throw new CustomException("Registration failed: " + ex.getMessage(), (HttpStatus) ex.getStatusCode());
        } catch (Exception ex) {
            throw new CustomException("An unexpected error occurred during registration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * User Login Endpoint
     * URL: POST /auth/login
     *
     * @param loginRequest containing username and password
     * @return ResponseEntity with JWT token and token type
     */
    @Operation(
            summary = "User Login",
            description = "Authenticate a user and obtain a JWT token.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Login successful"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "401",
                            description = "Invalid credentials"
                    )
            }
    )
    @SecurityRequirement(name = "")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate user via AUTH_SERVICE and obtain JWT token
            LoginResponse loginResponse = authServiceClient.login(loginRequest);

            return ResponseEntity.ok(loginResponse);
        } catch (HttpStatusCodeException ex) {
            throw new CustomException("Login failed", (HttpStatus) ex.getStatusCode());
        } catch (Exception ex) {
            throw new CustomException("An unexpected error occurred during login", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
