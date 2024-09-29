
package com.socialhub.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO representing a comment's details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    @Schema(description = "Unique identifier of the comment", example = "1")
    private Long id;

    @Schema(description = "ID of the post the comment belongs to", example = "10")
    private Long postId;

    @Schema(description = "Username of the comment's author", example = "john_doe")
    private String authorUsername;

    @Schema(description = "Content of the comment", example = "This is a comment.")
    private String content;

    @Schema(description = "Timestamp when the comment was created", example = "2023-10-04T12:34:56.789")
    private LocalDateTime createdAt;
}
