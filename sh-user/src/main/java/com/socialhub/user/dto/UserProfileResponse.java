package com.socialhub.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO representing user profile details.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {
    @Schema(description = "Unique identifier of the profile", example = "1")
    private Long id;

    @Schema(description = "Username of the user", example = "john_doe")
    private String username;

    @Schema(description = "Email address of the user", example = "john_doe@example.com")
    private String email;

    @Schema(description = "First name of the user", example = "John")
    private String firstName;

    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;

    @Schema(description = "Bio of the user", example = "Software developer and tech enthusiast.")
    private String bio;

    @Schema(description = "URL to the user's profile picture", example = "http://example.com/profile.jpg")
    private String profilePictureUrl;
}
