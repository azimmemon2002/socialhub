
package com.socialhub.user.repository;

import com.socialhub.user.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for Comment entity.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Finds all comments associated with a specific post.
     *
     * @param postId the ID of the post
     * @return List of comments on the post
     */
    List<Comment> findByPostId(Long postId);
}
