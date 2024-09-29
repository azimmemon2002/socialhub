package com.socialhub.user.repository;

import com.socialhub.user.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for Like entity.
 */
public interface LikeRepository extends JpaRepository<Like, Long> {

    /**
     * Checks if a user has already liked a specific post.
     *
     * @param postId the ID of the post
     * @param userId the ID of the user
     * @return true if the like exists, false otherwise
     */
    boolean existsByPostIdAndUserId(Long postId, Long userId);

    /**
     * Counts the number of likes for a specific post.
     *
     * @param postId the ID of the post
     * @return the count of likes
     */
    Long countByPostId(Long postId);

    /**
     * Finds all likes associated with a specific post.
     *
     * @param postId the ID of the post
     * @return List of Like entities
     */
    List<Like> findByPostId(Long postId);
}
