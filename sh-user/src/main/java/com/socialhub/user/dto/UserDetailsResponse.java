package com.socialhub.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.Set;

/**
 * DTO representing user details returned from auth_SERVICE.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsResponse {

    @Schema(description = "Unique identifier of the user", example = "1")
    private Long userId;

    @Schema(description = "Username of the user", example = "john_doe")
    private String username;

    @Schema(description = "Email address of the user", example = "john_doe@example.com")
    private String email;

    @Schema(description = "Roles assigned to the user", example = "[\"ROLE_USER\"]")
    private Set<String> roles;
}
