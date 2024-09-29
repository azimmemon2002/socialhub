package com.socialhub.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // Author
    private String content; // Text content
    private String mediaUrl; // Image or Video URL
    private String mediaType; // IMAGE, VIDEO, etc.

    private LocalDateTime createdAt;
}
