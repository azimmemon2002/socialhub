package com.socialhub.user.service;

import com.socialhub.user.dto.*;
import com.socialhub.user.entity.*;
import com.socialhub.user.exception.CustomException;
import com.socialhub.user.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service to handle post-related operations.
 */
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    /**
     * Creates a new post for the authenticated user.
     *
     * @param postRequest the post creation request
     * @param username    the username of the authenticated user
     * @return PostResponse containing the created post details
     */
    @Transactional
    public PostResponse createPost(PostRequest postRequest, String username) {
        User user = getUserByUsername(username);

        Post post = Post.builder()
                .userId(user.getId())
                .content(postRequest.getContent())
                .mediaUrl(postRequest.getMediaUrl())
                .mediaType(postRequest.getMediaType())
                .createdAt(LocalDateTime.now())
                .build();

        Post savedPost = postRepository.save(post);

        return mapToPostResponse(savedPost, user);
    }

    /**
     * Allows a user to like a post.
     *
     * @param postId   the ID of the post to like
     * @param username the username of the authenticated user
     */
    @Transactional
    public void likePost(Long postId, String username) {
        User user = getUserByUsername(username);
        Post post = getPostById(postId);

        // Check if the user has already liked the post
        if (likeRepository.existsByPostIdAndUserId(postId, user.getId())) {
            throw new CustomException("You have already liked this post.", HttpStatus.BAD_REQUEST);
        }

        Like like = Like.builder()
                .postId(postId)
                .userId(user.getId())
                .likedAt(LocalDateTime.now())
                .build();

        likeRepository.save(like);

    }

    /**
     * Allows a user to comment on a post.
     *
     * @param postId         the ID of the post to comment on
     * @param commentRequest the comment creation request
     * @param username       the username of the authenticated user
     * @return CommentResponse containing the created comment details
     */
    @Transactional
    public CommentResponse commentPost(Long postId, CommentRequest commentRequest, String username) {
        User user = getUserByUsername(username);
        Post post = getPostById(postId);

        Comment comment = Comment.builder()
                .postId(postId)
                .userId(user.getId())
                .content(commentRequest.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        Comment savedComment = commentRepository.save(comment);
        return mapToCommentResponse(savedComment, user);
    }

    /**
     * Retrieves all posts.
     *
     * @return List of PostResponse containing all posts
     */
    public List<PostResponse> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Post> posts = postRepository.findAll(pageable).getContent();
        return posts.stream()
                .map(post -> {
                    User user = getUserById(post.getUserId());
                    return mapToPostResponse(post, user);
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all posts created by a specific user.
     *
     * @param username the username of the user
     * @return List of PostResponse containing the user's posts
     */
    public PaginatedResponse<PostResponse> getPostsByUsername(String username, int page, int size) {
        User user = getUserByUsername(username);
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findByUserId(user.getId(), pageable);
        List<PostResponse> content = postPage.getContent().stream()
                .map(post -> mapToPostResponse(post, user))
                .collect(Collectors.toList());
        return PaginatedResponse.<PostResponse>builder()
                .content(content)
                .pageNumber(postPage.getNumber())
                .pageSize(postPage.getSize())
                .totalPages(postPage.getTotalPages())
                .totalElements(postPage.getTotalElements())
                .build();
    }
    /**
     * Deletes a post created by the authenticated user.
     *
     * @param postId   the ID of the post to delete
     * @param username the username of the authenticated user
     */
    @Transactional
    public void deletePost(Long postId, String username) {
        User user = getUserByUsername(username);
        Post post = getPostById(postId);

        if (!post.getUserId().equals(user.getId())) {
            throw new CustomException("You are not authorized to delete this post.", HttpStatus.FORBIDDEN);
        }

        postRepository.delete(post);
    }

    /**
     * Retrieves all comments associated with a specific post.
     *
     * @param postId the ID of the post
     * @return List of CommentResponse DTOs
     */
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        Post post = getPostById(postId); // Ensure the post exists

        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(comment -> {
                    User user = getUserById(comment.getUserId());
                    return CommentResponse.builder()
                            .id(comment.getId())
                            .postId(comment.getPostId())
                            .authorUsername(user.getUsername())
                            .content(comment.getContent())
                            .createdAt(comment.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all likes associated with a specific post.
     *
     * @param postId the ID of the post
     * @return List of LikeResponse DTOs
     */
    public List<LikeResponse> getLikesByPostId(Long postId) {
        Post post = getPostById(postId); // Ensure the post exists

        List<Like> likes = likeRepository.findByPostId(postId);
        return likes.stream()
                .map(like -> {
                    User user = getUserById(like.getUserId());
                    return LikeResponse.builder()
                            .id(like.getId())
                            .username(user.getUsername())
                            .likedAt(like.getLikedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a user by username.
     *
     * @param username the username to search for
     * @return User entity
     */
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("User not found with username: " + username, HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves a user by ID.
     *
     * @param userId the ID of the user
     * @return User entity
     */
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found with ID: " + userId, HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves a post by ID.
     *
     * @param postId the ID of the post
     * @return Post entity
     */
    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("Post not found with ID: " + postId, HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves a list of friend usernames for a given user ID.
     *
     * @param userId the ID of the user
     * @return List of friend usernames
     */
    private List<String> getFriendsUsernames(Long userId) {
        List<Friend> friends = friendRepository.findByUserIdAndStatus(userId, Friend.Status.ACCEPTED);
        return friends.stream()
                .map(friend -> getUserById(friend.getFriendId()).getUsername())
                .collect(Collectors.toList());
    }

    /**
     * Maps Post entity to PostResponse DTO.
     *
     * @param post the Post entity
     * @param user the User entity (author)
     * @return PostResponse DTO
     */
    private PostResponse mapToPostResponse(Post post, User user) {
        return PostResponse.builder()
                .id(post.getId())
                .authorUsername(user.getUsername())
                .content(post.getContent())
                .mediaUrl(post.getMediaUrl())
                .mediaType(post.getMediaType())
                .createdAt(post.getCreatedAt())
                .likeCount(likeRepository.countByPostId(post.getId()))
                .commentCount((long) commentRepository.findByPostId(post.getId()).size())
                .build();
    }

    /**
     * Maps Comment entity to CommentResponse DTO.
     *
     * @param comment the Comment entity
     * @param user    the User entity (author of the comment)
     * @return CommentResponse DTO
     */
    private CommentResponse mapToCommentResponse(Comment comment, User user) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .authorUsername(user.getUsername())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }


}
