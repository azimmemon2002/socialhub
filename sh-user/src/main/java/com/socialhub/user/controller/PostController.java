package com.socialhub.user.controller;

import com.socialhub.user.dto.*;
import com.socialhub.user.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controller to handle post-related endpoints.
 */
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "Post Controller", description = "Endpoints for managing posts")
@SecurityRequirement(name = "Bearer Authentication")
public class PostController {

    private final PostService postService;

    /**
     * Create a new post.
     * URL: POST /posts/create
     *
     * @param postRequest the post creation request containing content and optional media
     * @param jwt the JWT token of the authenticated user
     * @return ResponseEntity with the created Post
     */
    @Operation(summary = "Create a new post", description = "Allows an authenticated user to create a new post with content and optional media.")
    @PostMapping("/create")
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest postRequest,
                                                   @AuthenticationPrincipal Jwt jwt) {
        PostResponse post = postService.createPost(postRequest, jwt.getSubject());
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    /**
     * Like a post.
     * URL: POST /posts/{postId}/like
     *
     * @param postId the ID of the post to like
     * @param jwt the JWT token of the authenticated user
     * @return ResponseEntity with success message
     */
    @Operation(summary = "Like a post", description = "Allows an authenticated user to like a specific post.")
    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId,
                                           @AuthenticationPrincipal Jwt jwt) {
        postService.likePost(postId, jwt.getSubject());
        return ResponseEntity.ok("Post liked successfully");
    }

    /**
     * Comment on a post.
     * URL: POST /posts/{postId}/comment
     *
     * @param postId the ID of the post to comment on
     * @param commentRequest the comment creation request containing the comment content
     * @param jwt the JWT token of the authenticated user
     * @return ResponseEntity with the created Comment
     */
    @Operation(summary = "Comment on a post", description = "Allows an authenticated user to add a comment to a specific post.")
    @PostMapping("/{postId}/comment")
    public ResponseEntity<CommentResponse> commentOnPost(@PathVariable Long postId,
                                                         @Valid @RequestBody CommentRequest commentRequest,
                                                         @AuthenticationPrincipal Jwt jwt) {
        CommentResponse comment = postService.commentPost(postId, commentRequest, jwt.getSubject());
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    /**
     * Retrieve all posts.
     * URL: GET /posts
     *
     * @return ResponseEntity with a list of all posts
     */
    @Operation(summary = "Retrieve all posts", description = "Fetches a paginated list of all posts.")
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<PostResponse> posts = postService.getAllPosts(page, size);
        return ResponseEntity.ok(posts);
    }

    /**
     * Retrieve posts by a specific user.
     * URL: GET /posts/user/{username}
     *
     * @param username the username of the user whose posts are to be fetched
     * @return ResponseEntity with a list of posts by the specified user
     */
    @Operation(
            summary = "Retrieve posts by user",
            description = "Fetches a paginated list of posts created by a specific user.",
            parameters = {
                    @Parameter(name = "page", description = "Page number (zero-based)", example = "0"),
                    @Parameter(name = "size", description = "Number of items per page", example = "10")
            }
    )
    @GetMapping("/user/{username}")
    public ResponseEntity<PaginatedResponse<PostResponse>> getPostsByUser(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginatedResponse<PostResponse> response = postService.getPostsByUsername(username, page, size);
        return ResponseEntity.ok(response);
    }
    /**
     * Delete a post.
     * URL: DELETE /posts/{postId}
     *
     * @param postId the ID of the post to delete
     * @param jwt the JWT token of the authenticated user
     * @return ResponseEntity with success message
     */
    @Operation(summary = "Delete a post", description = "Allows the author of a postotalElementst to delete it.")
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId,
                                             @AuthenticationPrincipal Jwt jwt) {
        postService.deletePost(postId, jwt.getSubject());
        return ResponseEntity.ok("Post deleted successfully");
    }

    /**
     * Retrieve all comments for a specific post.
     * URL: GET /posts/{postId}/comments
     *
     * @param postId the ID of the post
     * @return ResponseEntity with a list of comments
     */
    @Operation(summary = "Retrieve comments for a post", description = "Fetches a list of all comments on a specific post, including commenter usernames and content.")
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsForPost(@PathVariable Long postId) {
        List<CommentResponse> comments = postService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    /**
     * Retrieve all likes for a specific post.
     * URL: GET /posts/{postId}/likes
     *
     * @param postId the ID of the post
     * @return ResponseEntity with a list of likes
     */
    @Operation(summary = "Retrieve likes for a post", description = "Fetches a list of all likes on a specific post, including usernames of users who liked the post.")
    @GetMapping("/{postId}/likes")
    public ResponseEntity<List<LikeResponse>> getLikesForPost(@PathVariable Long postId) {
        List<LikeResponse> likes = postService.getLikesByPostId(postId);
        return ResponseEntity.ok(likes);
    }
}
