package com.socialhub.user.controller;

import com.socialhub.user.dto.UserProfileResponse;
import com.socialhub.user.dto.UserProfileUpdateRequest;
import com.socialhub.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller to handle user profile-related endpoints.
 */
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@Tag(name = "Profile Controller", description = "Endpoints for managing user profiles")
@SecurityRequirement(name = "Bearer Authentication")
public class ProfileController {

    private final ProfileService profileService;

    /**
     * Get the authenticated user's profile.
     * URL: GET /profile/me
     *
     * @param jwt the JWT token of the authenticated user
     * @return UserProfileResponse containing profile details
     */
    @Operation(summary = "Get my profile", description = "Retrieve the profile of the authenticated user")
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        UserProfileResponse profile = profileService.getProfileByUsername(jwt.getSubject());
        return ResponseEntity.ok(profile);
    }

    /**
     * Update the authenticated user's profile.
     * URL: PUT /profile/me
     *
     * @param updateRequest containing updated profile details
     * @param jwt the JWT token of the authenticated user
     * @return ResponseEntity with success message
     */
    @Operation(summary = "Update my profile", description = "Update the profile of the authenticated user")
    @PutMapping("/me")
    public ResponseEntity<String> updateMyProfile(@Valid @RequestBody UserProfileUpdateRequest updateRequest,
                                                  @AuthenticationPrincipal Jwt jwt) {
        profileService.updateProfile(jwt.getSubject(), updateRequest);
        return ResponseEntity.ok("Profile updated successfully");
    }

    /**
     * Get any user's profile by ID.
     * URL: GET /profile/{id}
     *
     * Accessible only by users with ADMIN role.
     *
     * @param id the ID of the profile to retrieve
     * @return UserProfileResponse containing profile details
     */
    @Operation(summary = "Get user profile by ID", description = "Retrieve any user's profile by their profile ID. Admins only.")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileResponse> getProfileById(@PathVariable Long id) {
        UserProfileResponse profile = profileService.getProfileById(id);
        return ResponseEntity.ok(profile);
    }

    /**
     * View a user's profile by ID.
     * URL: GET /profile/view/{id}
     *
     * Accessible by any authenticated user.
     *
     * @param id the ID of the profile to view
     * @return UserProfileResponse containing profile details
     */
    @Operation(summary = "View user profile by ID", description = "View any user's profile by their profile ID. Accessible by any authenticated user.")
    @GetMapping("/view/{id}")
    public ResponseEntity<UserProfileResponse> viewProfile(@PathVariable Long id) {
        UserProfileResponse profile = profileService.getProfileById(id);
        return ResponseEntity.ok(profile);
    }
}
