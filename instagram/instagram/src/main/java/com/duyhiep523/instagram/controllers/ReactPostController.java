package com.duyhiep523.instagram.controllers;

import com.duyhiep523.instagram.response.Response;
import com.duyhiep523.instagram.services.Iservices.IReactionPostService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${apiPrefix}/reactions")
@RequiredArgsConstructor
public class ReactPostController {
    private final IReactionPostService reactionService;

    @PostMapping("/{userId}/{postId}")
    public ResponseEntity<?> addReactionToPost(@PathVariable String userId, @PathVariable String postId) {
        reactionService.addReactionToPost(userId, postId);
        Response<Object> response = Response.builder()
                .code(HttpStatus.CREATED.value())
                .message("Thả cảm xúc thành công")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @GetMapping("/count/{postId}")
    public ResponseEntity<?> getReactionCount(@PathVariable String postId) {
        int count = reactionService.getReactionCountByPost(postId);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Lấy số lượng cảm xúc thành công")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Kiểm tra xem người dùng đã thả cảm xúc cho bài viết hay chưa
     *
     * @param userId ID của người dùng
     * @param postId ID của bài viết
     * @return true nếu đã thả cảm xúc, false nếu chưa
     */
    @GetMapping("/check/{userId}/{postId}")
    public ResponseEntity<?> checkUserReaction(@PathVariable String userId, @PathVariable String postId) {
        boolean hasReacted = reactionService.hasUserReactedToPost(userId, postId);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message(hasReacted ? "Người dùng đã thả cảm xúc." : "Người dùng chưa thả cảm xúc.")
                .data(hasReacted)
                .build();
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{userId}/{postId}")
    public ResponseEntity<?> removeReaction(@PathVariable String userId, @PathVariable String postId) {
        reactionService.removeReactionFromPost(userId, postId);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Đã xoá cảm xúc thành công")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}
