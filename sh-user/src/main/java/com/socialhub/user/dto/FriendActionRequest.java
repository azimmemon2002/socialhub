
package com.socialhub.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for handling friend actions (ACCEPT/DECLINE).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendActionRequest {

    @NotNull(message = "Request ID cannot be null")
    @Schema(description = "ID of the friend request", example = "5")
    private Long requestId;

    @NotNull(message = "Action type cannot be null")
    @Schema(description = "Action to perform on the friend request", example = "ACCEPT")
    private ActionType actionType;

    public enum ActionType {
        ACCEPT,
        DECLINE
    }
}
