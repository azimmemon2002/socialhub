package com.socialhub.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    
    @NotBlank(message = "Username is mandatory")
    @Size(min = 4, max = 50)
    @Schema(description = "Desired username of the new user", example = "john_doe")
    private String username;
    
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Schema(description = "Email address of the new user", example = "john_doe@example.com")
    private String email;
    
    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password should have at least 6 characters")
    @Schema(description = "Desired password for the new account", example = "password123")
    private String password;
}
