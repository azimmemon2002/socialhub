package com.socialhub.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for sending a friend request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequest {
    @NotNull(message = "Friend ID cannot be null")
    @Schema(description = "ID of the user to send a friend request to", example = "2")
    private Long friendId;
}
