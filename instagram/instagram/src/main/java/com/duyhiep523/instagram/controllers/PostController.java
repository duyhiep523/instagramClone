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



    @GetMapping("/detail/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable String postId) {
        PostResponse postResponse = postService.getPostById(postId);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Lấy chi tiết bài viết thành công")
                .data(postResponse)
                .build();
        return ResponseEntity.ok(response);
    }




    /**
     * Đếm số lượng bài viết của một người dùng.
     *
     * @param userId ID của người dùng.
     * @return ResponseEntity chứa số lượng bài viết.
     */
    @GetMapping("/count/{userId}") // <-- Thêm endpoint này
    public ResponseEntity<?> countPostsByUserId(@PathVariable String userId) {
        long postCount = postService.countPostsByUserId(userId);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Đếm số lượng bài viết thành công")
                .data(postCount)
                .build();
        return ResponseEntity.ok(response);
    }
    /**
     * Lấy danh sách bài viết cho New Feed của người dùng.
     * Bao gồm bài viết từ người dùng đang theo dõi và bài viết công khai khác.
     *
     * @param userId ID của người dùng hiện tại.
     * @param page   Số trang (bắt đầu từ 0).
     * @param size   Kích thước trang.
     * @return ResponseEntity chứa danh sách bài viết cho New Feed.
     */
    @GetMapping("/feed/{userId}") // Endpoint mới cho New Feed
    public ResponseEntity<?> getFeedPosts(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<PostResponse> feedPosts = postService.getFeedPosts(userId, page, size);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách bài viết New Feed thành công")
                .data(feedPosts)
                .build();
        return ResponseEntity.ok(response);
    }
}
