package com.socialhub.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO for login requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "Username is mandatory")
    @Schema(description = "Username of the user", example = "john_doe")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Schema(description = "Password of the user", example = "password123")
    private String password;
}
