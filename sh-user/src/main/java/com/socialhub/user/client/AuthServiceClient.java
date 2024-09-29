package com.socialhub.user.client;

import com.socialhub.user.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Feign client to communicate with the AUTH_SERVICE.
 */
@FeignClient(name = "AUTH-SERVICE")
public interface AuthServiceClient {

    /**
     * Endpoint to authenticate user and obtain JWT token.
     *
     * @param loginRequest containing username and password
     * @return LoginResponse containing JWT token and token type
     */
    @PostMapping("/auth/login")
    LoginResponse login(@RequestBody LoginRequest loginRequest);

    /**
     * Endpoint to register a new user.
     *
     * @param registerRequest containing user registration details
     * @return UserDetailsResponse containing registered user information
     */
    @PostMapping("/auth/register")
    UserDetailsResponse register(@RequestBody RegisterRequest registerRequest);

}