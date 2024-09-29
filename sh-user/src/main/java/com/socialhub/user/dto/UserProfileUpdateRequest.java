package com.socialhub.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO for updating user profile details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileUpdateRequest {

    @Size(max = 50, message = "First name cannot exceed 50 characters")
    @Schema(description = "First name of the user", example = "John")
    private String firstName;

    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;

    @Size(max = 250, message = "Bio cannot exceed 250 characters")
    @Schema(description = "Short biography or description of the user", example = "Software developer and tech enthusiast.")
    private String bio;

    @Size(max = 255, message = "Profile picture URL cannot exceed 255 characters")
    @Schema(description = "URL to the user's profile picture", example = "http://example.com/profile.jpg")
    private String profilePictureUrl;
}
