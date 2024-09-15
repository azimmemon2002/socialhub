package com.socialhub.auth.service;

import com.socialhub.auth.dto.LoginRequest;
import com.socialhub.auth.dto.LoginResponse;
import com.socialhub.auth.dto.UserDetailsResponse;
import com.socialhub.auth.entity.User;
import com.socialhub.auth.exception.CustomException;
import com.socialhub.auth.repository.UserRepository;
import com.socialhub.auth.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    /**
     * Authenticates the user and generates a JWT token upon successful authentication.
     *
     * @param loginRequest the login request containing username and password
     * @return LoginResponse containing the JWT token and token type
     */
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (Exception ex) {
            throw new CustomException("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return LoginResponse.builder()
                .token(jwt)
                .tokenType("Bearer")
                .build();
    }

    /**
     * Validates the JWT token and retrieves user details if valid.
     *
     * @param token the JWT token
     * @return UserDetailsResponse containing user information
     */
    public UserDetailsResponse validateToken(String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new CustomException("Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }

        String username = tokenProvider.getUsernameFromJWT(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());

        return UserDetailsResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }
}
