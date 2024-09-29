package com.socialhub.user.service;

import com.socialhub.user.dto.UserProfileResponse;
import com.socialhub.user.dto.UserProfileUpdateRequest;
import com.socialhub.user.entity.Profile;
import com.socialhub.user.entity.User;
import com.socialhub.user.exception.ResourceNotFoundException;
import com.socialhub.user.repository.ProfileRepository;
import com.socialhub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

/**
 * Service to handle user profile-related operations.
 */
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    /**
     * Retrieves a user's profile by their username.
     *
     * @param username the username of the user
     * @return UserProfileResponse containing profile details
     */
    public UserProfileResponse getProfileByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user: " + username));

        return mapToUserProfileResponse(user, profile);
    }

    /**
     * Retrieves a user's profile by their profile ID.
     *
     * @param profileId the ID of the profile
     * @return UserProfileResponse containing profile details
     */
    public UserProfileResponse getProfileById(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with ID: " + profileId));
        User user = profile.getUser();

        return mapToUserProfileResponse(user, profile);
    }

    /**
     * Updates the authenticated user's profile based on the provided update request.
     *
     * @param username the username of the authenticated user
     * @param updateRequest containing updated profile details
     */
    @Transactional
    public void updateProfile(String username, UserProfileUpdateRequest updateRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user: " + username));

        // Update profile fields if present in the request
        if (updateRequest.getFirstName() != null) {
            profile.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            profile.setLastName(updateRequest.getLastName());
        }
        if (updateRequest.getBio() != null) {
            profile.setBio(updateRequest.getBio());
        }
        if (updateRequest.getProfilePictureUrl() != null) {
            profile.setProfilePictureUrl(updateRequest.getProfilePictureUrl());
        }

        profileRepository.save(profile);
    }

    /**
     * Maps User and Profile entities to UserProfileResponse DTO.
     *
     * @param user the User entity
     * @param profile the Profile entity
     * @return UserProfileResponse DTO
     */
    private UserProfileResponse mapToUserProfileResponse(User user, Profile profile) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .bio(profile.getBio())
                .profilePictureUrl(profile.getProfilePictureUrl())
                .build();
    }
}
