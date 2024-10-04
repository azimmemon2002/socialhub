package com.socialhub.user.service;

import com.socialhub.user.dto.UserDetailsResponse;
import com.socialhub.user.entity.Profile;
import com.socialhub.user.entity.User;
import com.socialhub.user.exception.CustomException;
import com.socialhub.user.repository.ProfileRepository;
import com.socialhub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Optional;

/**
 * Service to handle user-related operations beyond authentication,
 * such as creating and managing user profiles.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    /**
     * Creates a user profile in user_SERVICE based on the registered user details from AUTH_SERVICE.
     *
     * @param authResponse UserDetailsResponse from AUTH_SERVICE after registration
     */
    @Transactional
    public void createUserProfile(UserDetailsResponse authResponse) {
        // Check if user profile already exists
        Optional<User> existingUser = userRepository.findByAuthUserId(authResponse.getUserId());
        if (existingUser.isPresent()) {
            throw new CustomException("User profile already exists", HttpStatus.BAD_REQUEST);
        }

        // Create new User entity
        User user = User.builder()
                .authUserId(authResponse.getUserId())
                .username(authResponse.getUsername())
                .email(authResponse.getEmail())
                .build();

        User savedUser = userRepository.save(user);

        // Initialize default profile
        Profile profile = Profile.builder()
                .user(savedUser)
                .firstName("")
                .lastName("")
                .bio("")
                .profilePictureUrl("")
                .build();

        profileRepository.save(profile);
    }
}
