
package com.socialhub.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO representing a post's details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    @Schema(description = "Unique identifier of the post", example = "10")
    private Long id;

    @Schema(description = "Username of the post's author", example = "john_doe")
    private String authorUsername;

    @Schema(description = "Content of the post", example = "Hello, world!")
    private String content;

    @Schema(description = "URL of the media attached to the post", example = "http://example.com/image.jpg")
    private String mediaUrl;

    @Schema(description = "Type of the media (e.g., IMAGE, VIDEO)", example = "IMAGE")
    private String mediaType;

    @Schema(description = "Timestamp when the post was created", example = "2023-10-04T12:34:56.789")
    private LocalDateTime createdAt;

    @Schema(description = "Number of likes on the post", example = "25")
    private Long likeCount;

    @Schema(description = "Number of comments on the post", example = "5")
    private Long commentCount;
}
