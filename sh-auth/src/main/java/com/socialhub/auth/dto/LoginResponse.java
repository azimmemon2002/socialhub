package com.socialhub.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    
    @Schema(description = "JWT token issued upon successful authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "Type of the token issued", example = "Bearer")
    private String tokenType = "Bearer";
}