package com.duyhiep523.instagram.controllers;

import com.duyhiep523.instagram.dtos.request.post.PostRequest;
import com.duyhiep523.instagram.dtos.response.post.PostResponse;
import com.duyhiep523.instagram.response.Response;
import com.duyhiep523.instagram.services.Iservices.IPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/posts")
@RequiredArgsConstructor
public class PostController {
    private final IPostService postService;

    /**
     * Tạo bài viết mới.
     *
     * @param userId  ID của người dùng tạo bài viết.
     * @param request DTO chứa thông tin bài viết.
     * @return ResponseEntity chứa thông tin phản hồi.
     */
    @PostMapping("/{userId}")
    public ResponseEntity<?> createPost(
            @PathVariable String userId,
            @Valid @ModelAttribute PostRequest request) {
        PostResponse postResponse = postService.createPost(userId, request);
        Response<Object> response = Response.builder()
                .code(HttpStatus.CREATED.value())
                .message("Tạo bài viết thành công")
                .data(postResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Lấy danh sách bài viết của người dùng.
     *
     * @param userId ID của người dùng.
     * @return ResponseEntity chứa danh sách bài viết.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getPostsByUserId(@PathVariable String userId) {
        List<PostResponse> posts = postService.getPostsByUserId(userId);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách bài viết thành công")
                .data(posts)
                .build();
        return ResponseEntity.ok(response);
    }
}
