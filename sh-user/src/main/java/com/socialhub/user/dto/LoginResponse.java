package com.socialhub.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO representing the response from auth_SERVICE's /auth/login endpoint.
 * Contains both the JWT token and its type.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    @Schema(description = "JWT token issued upon successful authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Type of the token issued", example = "Bearer")
    private String tokenType;
}
