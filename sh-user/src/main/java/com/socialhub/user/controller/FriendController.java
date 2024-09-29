package com.socialhub.user.controller;

import com.socialhub.user.dto.*;
import com.socialhub.user.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller to handle friend-related endpoints.
 */
@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
@Tag(name = "Friend Controller", description = "Endpoints for managing friends and friend requests")
@SecurityRequirement(name = "Bearer Authentication")
public class FriendController {

    private final FriendService friendService;

    /**
     * Send a friend request to another user.
     * URL: POST /friends/request
     *
     * @param friendRequest containing the ID of the user to be friended
     * @param jwt the JWT token of the authenticated user
     * @return ResponseEntity with success message
     */
    @Operation(summary = "Send Friend Request", description = "Send a friend request to another user by their user ID.")
    @PostMapping("/request")
    public ResponseEntity<String> sendFriendRequest(@Valid @RequestBody FriendRequest friendRequest,
                                                    @AuthenticationPrincipal Jwt jwt) {
        friendService.sendFriendRequest(jwt.getSubject(), friendRequest.getFriendId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Friend request sent successfully");
    }

    /**
     * Accept or decline a received friend request.
     * URL: POST /friends/action
     *
     * @param actionRequest containing the request ID and action type (ACCEPT/DECLINE)
     * @param jwt the JWT token of the authenticated user
     * @return ResponseEntity with success message
     */
    @Operation(summary = "Handle Friend Request", description = "Accept or decline a received friend request.")
    @PostMapping("/action")
    public ResponseEntity<String> handleFriendRequest(@Valid @RequestBody FriendActionRequest actionRequest,
                                                      @AuthenticationPrincipal Jwt jwt) {
        switch (actionRequest.getActionType()) {
            case ACCEPT:
                friendService.acceptFriendRequest(jwt.getSubject(), actionRequest.getRequestId());
                return ResponseEntity.ok("Friend request accepted");
            case DECLINE:
                friendService.declineFriendRequest(jwt.getSubject(), actionRequest.getRequestId());
                return ResponseEntity.ok("Friend request declined");
            default:
                return ResponseEntity.badRequest().body("Invalid action type");
        }
    }

    /**
     * List all friends of the authenticated user.
     * URL: GET /friends/list
     *
     * @param jwt the JWT token of the authenticated user
     * @return ResponseEntity with a list of friends
     */
    @Operation(summary = "List Friends", description = "Retrieve a list of all friends of the authenticated user.")
    @GetMapping("/list")
    public ResponseEntity<FriendListResponse> listFriends(@AuthenticationPrincipal Jwt jwt) {
        List<String> friends = friendService.getFriends(jwt.getSubject());
        return ResponseEntity.ok(FriendListResponse.builder().friends(friends).build());
    }

    /**
     * List all received friend requests of the authenticated user.
     * URL: GET /friends/requests/received
     *
     * @param jwt the JWT token of the authenticated user
     * @return ResponseEntity with a list of received friend requests
     */
    @Operation(summary = "List Received Friend Requests", description = "Retrieve a list of all friend requests received by the authenticated user.")
    @GetMapping("/requests/received")
    public ResponseEntity<List<FriendResponse>> listReceivedRequests(@AuthenticationPrincipal Jwt jwt) {
        List<FriendResponse> receivedRequests = friendService.getReceivedFriendRequests(jwt.getSubject());
        return ResponseEntity.ok(receivedRequests);
    }

    /**
     * List all sent friend requests of the authenticated user.
     * URL: GET /friends/requests/sent
     *
     * @param jwt the JWT token of the authenticated user
     * @return ResponseEntity with a list of sent friend requests
     */
    @Operation(summary = "List Sent Friend Requests", description = "Retrieve a list of all friend requests sent by the authenticated user.")
    @GetMapping("/requests/sent")
    public ResponseEntity<List<FriendResponse>> listSentRequests(@AuthenticationPrincipal Jwt jwt) {
        List<FriendResponse> sentRequests = friendService.getSentFriendRequests(jwt.getSubject());
        return ResponseEntity.ok(sentRequests);
    }

    /**
     * Remove a friend from the authenticated user's friend list.
     * URL: DELETE /friends/remove/{friendId}
     *
     * @param friendId the ID of the friend to be removed
     * @param jwt the JWT token of the authenticated user
     * @return ResponseEntity with success message
     */
    @Operation(summary = "Remove Friend", description = "Remove a friend from the authenticated user's friend list by their user ID.")
    @DeleteMapping("/remove/{friendId}")
    public ResponseEntity<String> removeFriend(@PathVariable Long friendId,
                                               @AuthenticationPrincipal Jwt jwt) {
        friendService.removeFriend(jwt.getSubject(), friendId);
        return ResponseEntity.ok("Friend removed successfully");
    }
}
