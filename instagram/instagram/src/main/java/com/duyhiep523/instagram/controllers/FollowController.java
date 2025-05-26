package com.duyhiep523.instagram.controllers;

import com.duyhiep523.instagram.dtos.request.follow.FollowRequest;

import com.duyhiep523.instagram.dtos.response.follow.FollowerResponse;
import com.duyhiep523.instagram.response.Response;
import com.duyhiep523.instagram.services.Iservices.IFollowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/follows")
@RequiredArgsConstructor
public class FollowController {
    private final IFollowService followService;

    /**
     * theo dõi một người dùng
     *
     * @param followerId ID of the follower
     * @param request    FollowRequest containing the ID of the user to follow
     */
    @PostMapping("/{followerId}/follow")
    public ResponseEntity<?> followUser(@PathVariable String followerId,
                                        @Valid @RequestBody FollowRequest request) {
        followService.followUser(followerId, request);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Theo dõi thành công")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * hủy theo dõi một người dùng
     *
     * @param followerId  ID of the follower
     * @param followingId ID of the user to unfollow
     */
    @DeleteMapping("/{followerId}/unfollow/{followingId}")
    public ResponseEntity<?> unfollowUser(@PathVariable String followerId,
                                          @PathVariable String followingId) {
        followService.unfollowUser(followerId, followingId);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Hủy theo dõi thành công")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * kiểm tra trạng thái theo dõi giữa hai người dùng
     *
     * @param currentUserId ID of the current user
     * @param targetUserId  ID of the target user
     */
    @GetMapping("/relationship")
    public ResponseEntity<?> checkRelationship(@RequestParam String currentUserId, @RequestParam String targetUserId) {
        String status = followService.getFollowStatus(currentUserId, targetUserId);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Trạng thái quan hệ")
                .data(status)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * lấy danh sách người theo dõi của một người dùng
     *
     * @param userId ID of the user
     */
    @GetMapping("/followers")
    public ResponseEntity<?> getFollowers(@RequestParam String userId) {
        List<FollowerResponse> followers = followService.getFollowers(userId);

        Response<List<FollowerResponse>> response = Response.<List<FollowerResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Danh sách người theo dõi")
                .data(followers)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * lấy danh sách những người mà một người dùng đang theo dõi
     *
     * @param userId ID of the user
     */
    @GetMapping("/following")
    public ResponseEntity<?> getFollowing(@RequestParam String userId) {
        List<FollowerResponse> following = followService.getFollowing(userId);

        Response<List<FollowerResponse>> response = Response.<List<FollowerResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Danh sách người đang theo dõi")
                .data(following)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * đếm số lượng người theo dõi của một người dùng
     *
     * @param userId ID of the user
     */
    @GetMapping("/followers/count")
    public ResponseEntity<?> countFollowers(@RequestParam String userId) {
        long count = followService.countFollowers(userId);

        Response<Long> response = Response.<Long>builder()
                .code(HttpStatus.OK.value())
                .message("Số lượng người theo dõi")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * đếm số lượng người mà một người dùng đang theo dõi
     *
     * @param userId ID of the user
     */
    @GetMapping("/following/count")
    public ResponseEntity<?> countFollowing(@RequestParam String userId) {
        long count = followService.countFollowing(userId);

        Response<Long> response = Response.<Long>builder()
                .code(HttpStatus.OK.value())
                .message("Số lượng người dùng đang theo dõi")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * lấy danh sách người dùng gợi ý theo dõi
     *
     * @param userId ID of the user
     */
    @GetMapping("/suggested")
    public ResponseEntity<?> getSuggestedUsers(@RequestParam String userId) {
        List<FollowerResponse> suggestedUsers = followService.getSuggestedUsers(userId);

        Response<List<FollowerResponse>> response = Response.<List<FollowerResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Danh sách gợi ý theo dõi")
                .data(suggestedUsers)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Lấy danh sách những người theo dõi bạn nhưng bạn chưa theo dõi lại
     *
     * @param currentUserId ID của người dùng hiện tại
     */
    @GetMapping("/followers/not-followed-back")
    public ResponseEntity<?> getFollowersNotFollowedBack(@RequestParam String currentUserId) {
        List<FollowerResponse> notFollowedBack = followService.getFollowersNotFollowedBack(currentUserId);

        Response<List<FollowerResponse>> response = Response.<List<FollowerResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Danh sách người theo dõi chưa được theo dõi lại")
                .data(notFollowedBack)
                .build();

        return ResponseEntity.ok(response);
    }


}
