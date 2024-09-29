
package com.socialhub.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO representing a like's details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeResponse {
    @Schema(description = "Unique identifier of the like", example = "15")
    private Long id;

    @Schema(description = "Username of the user who liked the post", example = "john_doe")
    private String username;

    @Schema(description = "Timestamp when the post was liked", example = "2023-10-04T12:34:56.789")
    private LocalDateTime likedAt;
}
