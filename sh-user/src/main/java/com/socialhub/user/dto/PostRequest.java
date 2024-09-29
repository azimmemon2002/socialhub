package com.socialhub.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {
    @NotBlank
    @Schema(description = "Content of the post", example = "Hello, world!")
    private String content;

    @Schema(description = "URL of the media attached to the post", example = "http://example.com/image.jpg")
    private String mediaUrl;

    @Schema(description = "Type of the media (e.g., IMAGE, VIDEO)", example = "IMAGE")
    private String mediaType;
}
