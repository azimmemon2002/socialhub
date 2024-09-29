package com.socialhub.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    @Schema(description = "Timestamp of the response", example = "2024-10-04T12:34:56.789")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "200")
    private int status;

    @Schema(description = "Message describing the result", example = "Operation successful")
    private String message;

    @Schema(description = "Request path", example = "/auth/login")
    private String path;
}
