package com.socialhub.user.repository;

import com.socialhub.user.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Friend entity.
 */
public interface FriendRepository extends JpaRepository<Friend, Long> {

    /**
     * Finds friends by user ID and status.
     *
     * @param userId the ID of the user
     * @param status the status of the friendship (e.g., ACCEPTED)
     * @return List of Friend entities
     */
    List<Friend> findByUserIdAndStatus(Long userId, Friend.Status status);

    /**
     * Checks if a friend request exists between two users with a specific status.
     *
     * @param userId the ID of the user sending the request
     * @param friendId the ID of the user receiving the request
     * @param status the status of the request (e.g., PENDING)
     * @return true if exists, false otherwise
     */
    boolean existsByUserIdAndFriendIdAndStatus(Long userId, Long friendId, Friend.Status status);

    /**
     * Finds a friend relationship by user ID, friend ID, and status.
     *
     * @param userId the ID of the user
     * @param friendId the ID of the friend
     * @param status the status of the relationship
     * @return Optional containing the Friend entity if found
     */
    Optional<Friend> findByUserIdAndFriendIdAndStatus(Long userId, Long friendId, Friend.Status status);

    /**
     * Finds all friend requests received by a user with a specific status.
     *
     * @param friendId the ID of the user receiving the requests
     * @param status the status of the requests (e.g., PENDING)
     * @return List of Friend entities
     */
    List<Friend> findByFriendIdAndStatus(Long friendId, Friend.Status status);
}
