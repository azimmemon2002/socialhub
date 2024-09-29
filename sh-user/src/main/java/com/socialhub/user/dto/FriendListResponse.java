
package com.socialhub.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.List;

/**
 * DTO representing a list of friends.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendListResponse {
    
    @Schema(description = "List of usernames of friends", example = "[\"alice\", \"bob\"]")
    private List<String> friends;
}
