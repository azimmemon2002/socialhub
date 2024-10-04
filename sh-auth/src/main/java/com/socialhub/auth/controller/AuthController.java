package com.socialhub.auth.controller;

import com.socialhub.auth.dto.*;
import com.socialhub.auth.service.AuthService;
import com.socialhub.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Endpoints for user authentication and registration")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private String extractJwtToken(String bearerToken) {
        return bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : bearerToken;
    }
    /**
     * User Registration Endpoint
     * URL: POST /auth/register
     */
    @Operation(summary = "Register a new user", responses = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDetailsResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        UserDetailsResponse response = userService.registerUser(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * User Login Endpoint
     * URL: POST /auth/login
     */
    @Operation(summary = "Authenticate user and issue JWT token")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    // /**
    //  * Token Validation Endpoint
    //  * URL: POST /auth/validate
    //  */
    // @Operation(summary = "Validate JWT Token", security = @SecurityRequirement(name = "Bearer Authentication"))
    // @PostMapping("/validate")
    // public ResponseEntity<UserDetailsResponse> validateToken(@RequestHeader(name = "Authorization") String token) {
    //     String jwtToken = extractJwtToken(token);
    //     UserDetailsResponse userDetails = authService.validateToken(jwtToken);
    //     return ResponseEntity.ok(userDetails);
    // }


    // /**
    //  * Optional: Role Management Endpoint
    //  * URL: GET /auth/roles
    //  */
    // @Operation(summary = "Get User Roles", security = @SecurityRequirement(name = "Bearer Authentication"))
    // @GetMapping("/roles")
    // public ResponseEntity<Set<String>> getUserRoles(@RequestHeader(name = "Authorization") String token) {
    //     String jwtToken = extractJwtToken(token);
    //     UserDetailsResponse userDetails = authService.validateToken(jwtToken);
    //     return ResponseEntity.ok(userDetails.getRoles());
    // }
}
