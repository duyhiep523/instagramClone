package com.duyhiep523.instagram.controllers;



import com.duyhiep523.instagram.dtos.request.comment.PostCommentRequest;
import com.duyhiep523.instagram.dtos.response.comment.PostCommentResponse;
import com.duyhiep523.instagram.response.Response;
import com.duyhiep523.instagram.services.Iservices.IPostCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/post-comments")
@RequiredArgsConstructor
public class PostCommentController {
    private final IPostCommentService postCommentService;

    @PostMapping("/{userId}/{postId}")
    public ResponseEntity<?> addCommentToPost(
            @PathVariable String userId,
            @PathVariable String postId,
            @Valid @RequestBody PostCommentRequest request) {
        PostCommentResponse commentResponse = postCommentService.addCommentToPost(userId, postId, request);
        Response<Object> response = Response.builder()
                .code(HttpStatus.CREATED.value())
                .message("Thêm bình luận thành công")
                .data(commentResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable String commentId,
            @Valid @RequestBody PostCommentRequest request) {
        PostCommentResponse commentResponse = postCommentService.updateComment(commentId, request);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Cập nhật bình luận thành công")
                .data(commentResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable String commentId) {
        postCommentService.deleteComment(commentId);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Xóa bình luận thành công")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getCommentsByPost(@PathVariable String postId) {
        List<PostCommentResponse> comments = postCommentService.getCommentsByPost(postId);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách bình luận thành công")
                .data(comments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/{postId}")
    public ResponseEntity<?> getCommentCount(@PathVariable String postId) {
        int count = postCommentService.countCommentsByPost(postId);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Lấy số lượng bình luận thành công")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }
}
