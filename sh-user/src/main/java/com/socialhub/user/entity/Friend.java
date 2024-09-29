package com.socialhub.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing a friend relationship or friend request.
 */
@Entity
@Table(name = "friends")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user who sent the friend request.
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * The user who received the friend request.
     */
    @Column(nullable = false)
    private Long friendId;

    /**
     * The status of the friend request.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    /**
     * Timestamp when the friend request was made.
     */
    private LocalDateTime requestedAt;

    public enum Status {
        PENDING,
        ACCEPTED,
        DECLINED
    }
}
