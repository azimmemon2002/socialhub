
package com.socialhub.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO for like post requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeRequest {
    @Schema(description = "ID of the post to like", example = "10")
    private Long postId;
}
