package com.socialhub.user.service;

import com.socialhub.user.dto.FriendActionRequest;
import com.socialhub.user.dto.FriendResponse;
import com.socialhub.user.entity.Friend;
import com.socialhub.user.entity.User;
import com.socialhub.user.exception.ResourceNotFoundException;
import com.socialhub.user.repository.FriendRepository;
import com.socialhub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service to handle friend-related operations.
 */
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    /**
     * Sends a friend request from the authenticated user to another user.
     *
     * @param username the username of the authenticated user
     * @param friendId the ID of the user to send a friend request to
     */
    @Transactional
    public void sendFriendRequest(String username, Long friendId) {
        User fromUser = getUserByUsername(username);
        User toUser = getUserById(friendId);

        if (fromUser.getId().equals(toUser.getId())) {
            throw new IllegalArgumentException("Cannot send friend request to yourself");
        }

        // Check if a friend request already exists
        boolean exists = friendRepository.existsByUserIdAndFriendIdAndStatus(fromUser.getId(), toUser.getId(), Friend.Status.PENDING);
        if (exists) {
            throw new IllegalArgumentException("Friend request already sent");
        }

        // Check if users are already friends
        boolean alreadyFriends = friendRepository.existsByUserIdAndFriendIdAndStatus(fromUser.getId(), toUser.getId(), Friend.Status.ACCEPTED);
        if (alreadyFriends) {
            throw new IllegalArgumentException("You are already friends with this user");
        }

        // Create a new friend request
        Friend friendRequest = Friend.builder()
                .userId(fromUser.getId())
                .friendId(toUser.getId())
                .status(Friend.Status.PENDING)
                .requestedAt(LocalDateTime.now())
                .build();

        friendRepository.save(friendRequest);

    }

    /**
     * Accepts a received friend request.
     *
     * @param username the username of the authenticated user
     * @param requestId the ID of the friend request to accept
     */
    @Transactional
    public void acceptFriendRequest(String username, Long requestId) {
        User toUser = getUserByUsername(username);
        Friend friendRequest = friendRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Friend request not found"));

        if (!friendRequest.getFriendId().equals(toUser.getId())) {
            throw new IllegalArgumentException("You are not authorized to accept this friend request");
        }

        if (friendRequest.getStatus() != Friend.Status.PENDING) {
            throw new IllegalArgumentException("Friend request is not pending");
        }

        // Update the status to ACCEPTED
        friendRequest.setStatus(Friend.Status.ACCEPTED);
        friendRepository.save(friendRequest);

        // Create reciprocal friend relationship
        Friend reciprocalFriend = Friend.builder()
                .userId(toUser.getId())
                .friendId(friendRequest.getUserId())
                .status(Friend.Status.ACCEPTED)
                .requestedAt(LocalDateTime.now())
                .build();
        friendRepository.save(reciprocalFriend);


    }

    /**
     * Declines a received friend request.
     *
     * @param username the username of the authenticated user
     * @param requestId the ID of the friend request to decline
     */
    @Transactional
    public void declineFriendRequest(String username, Long requestId) {
        User toUser = getUserByUsername(username);
        Friend friendRequest = friendRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Friend request not found"));

        if (!friendRequest.getFriendId().equals(toUser.getId())) {
            throw new IllegalArgumentException("You are not authorized to decline this friend request");
        }

        if (friendRequest.getStatus() != Friend.Status.PENDING) {
            throw new IllegalArgumentException("Friend request is not pending");
        }

        // Update the status to DECLINED
        friendRequest.setStatus(Friend.Status.DECLINED);
        friendRepository.save(friendRequest);

    }

    /**
     * Retrieves a list of accepted friends for the authenticated user.
     *
     * @param username the username of the authenticated user
     * @return List of friend usernames
     */
    public List<String> getFriends(String username) {
        User user = getUserByUsername(username);
        List<Friend> friends = friendRepository.findByUserIdAndStatus(user.getId(), Friend.Status.ACCEPTED);
        return friends.stream()
                .map(friend -> getUsernameById(friend.getFriendId()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of received (incoming) friend requests for the authenticated user.
     *
     * @param username the username of the authenticated user
     * @return List of FriendResponse DTOs representing received friend requests
     */
    public List<FriendResponse> getReceivedFriendRequests(String username) {
        User user = getUserByUsername(username);
        List<Friend> receivedRequests = friendRepository.findByFriendIdAndStatus(user.getId(), Friend.Status.PENDING);
        return receivedRequests.stream()
                .map(request -> FriendResponse.builder()
                        .requestId(request.getId())
                        .fromUsername(getUsernameById(request.getUserId()))
                        .toUsername(getUsernameById(request.getFriendId()))
                        .status(FriendResponse.Status.PENDING)
                        .requestedAt(request.getRequestedAt())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of sent (outgoing) friend requests by the authenticated user.
     *
     * @param username the username of the authenticated user
     * @return List of FriendResponse DTOs representing sent friend requests
     */
    public List<FriendResponse> getSentFriendRequests(String username) {
        User user = getUserByUsername(username);
        List<Friend> sentRequests = friendRepository.findByUserIdAndStatus(user.getId(), Friend.Status.PENDING);
        return sentRequests.stream()
                .map(request -> FriendResponse.builder()
                        .requestId(request.getId())
                        .fromUsername(getUsernameById(request.getUserId()))
                        .toUsername(getUsernameById(request.getFriendId()))
                        .status(FriendResponse.Status.PENDING)
                        .requestedAt(request.getRequestedAt())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Removes a friend from the authenticated user's friend list.
     *
     * @param username the username of the authenticated user
     * @param friendId the ID of the friend to be removed
     */
    @Transactional
    public void removeFriend(String username, Long friendId) {
        User user = getUserByUsername(username);
        User friend = getUserById(friendId);

        // Find the reciprocal friend relationship
        Friend reciprocalFriend = friendRepository.findByUserIdAndFriendIdAndStatus(user.getId(), friendId, Friend.Status.ACCEPTED)
                .orElseThrow(() -> new ResourceNotFoundException("Friend relationship not found"));

        // Delete the reciprocal friend relationship
        friendRepository.delete(reciprocalFriend);

        // Delete the original friend relationship if exists
        friendRepository.findByUserIdAndFriendIdAndStatus(friendId, user.getId(), Friend.Status.ACCEPTED)
                .ifPresent(friendRepository::delete);

    }

    /**
     * Retrieves a user by username.
     *
     * @param username the username to search for
     * @return User entity
     */
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user
     * @return User entity
     */
    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    /**
     * Retrieves a username by user ID.
     *
     * @param id the ID of the user
     * @return Username as a String
     */
    private String getUsernameById(Long id) {
        return getUserById(id).getUsername();
    }
}
