package com.socialhub.auth.service;

import com.socialhub.auth.dto.RegisterRequest;
import com.socialhub.auth.dto.UserDetailsResponse;
import com.socialhub.auth.entity.Role;
import com.socialhub.auth.entity.User;
import com.socialhub.auth.exception.CustomException;
import com.socialhub.auth.repository.RoleRepository;
import com.socialhub.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user and assigns the ROLE_USER by default.
     *
     * @param registerRequest the registration request containing user details
     * @return UserDetailsResponse containing registered user information
     */
    @Transactional
    public UserDetailsResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new CustomException("Username is already taken", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new CustomException("Email is already in use", HttpStatus.BAD_REQUEST);
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new CustomException("User Role not set.", HttpStatus.INTERNAL_SERVER_ERROR));

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .roles(Collections.singleton(userRole))
                .build();

        User savedUser = userRepository.save(user);

        Set<String> roles = savedUser.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return UserDetailsResponse.builder()
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .roles(roles)
                .build();
    }
}
