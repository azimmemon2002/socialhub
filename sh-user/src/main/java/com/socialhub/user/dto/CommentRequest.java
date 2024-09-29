
package com.socialhub.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO for comment creation requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest {
    
    @NotBlank(message = "Comment content cannot be empty")
    @Schema(description = "Content of the comment", example = "This is a comment.")
    private String content;
}
