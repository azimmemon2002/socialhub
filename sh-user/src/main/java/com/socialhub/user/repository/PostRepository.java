
package com.socialhub.user.repository;

import com.socialhub.user.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

/**
 * Repository interface for Post entity.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Finds all posts by a specific user with pagination.
     *
     * @param userId the ID of the user
     * @param pageable pagination information
     * @return Page of posts created by the user
     */
    Page<Post> findByUserId(Long userId, Pageable pageable);
}