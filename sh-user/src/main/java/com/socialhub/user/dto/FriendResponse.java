package com.socialhub.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO representing a friend or a friend request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendResponse {
    
    @Schema(description = "ID of the friend request", example = "5")
    private Long requestId;

    @Schema(description = "Username of the user who sent the request", example = "john_doe")
    private String fromUsername;

    @Schema(description = "Username of the user who received the request", example = "jane_smith")
    private String toUsername;

    @Schema(description = "Status of the friend request", example = "PENDING")
    private Status status;

    @Schema(description = "Timestamp when the friend request was sent", example = "2023-10-04T12:34:56.789")
    private LocalDateTime requestedAt;

    public enum Status {
        PENDING,
        ACCEPTED,
        DECLINED
    }
}
