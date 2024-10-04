package com.socialhub.user.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a user in user_SERVICE.
 * Linked to AUTH_SERVICE via authUserId.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reference to the user in AUTH_SERVICE.
     */
    @Column(unique = true, nullable = false)
    private Long authUserId;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

}
