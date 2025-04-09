package com.duyhiep523.instagram.controllers;

import com.duyhiep523.instagram.dtos.request.highlightStory.HighlightStoryRequest;
import com.duyhiep523.instagram.dtos.response.highlightStory.HighlightStoryResponse;
import com.duyhiep523.instagram.dtos.response.highlightStory.HighlightStoryResponseDetail;
import com.duyhiep523.instagram.response.Response;
import com.duyhiep523.instagram.services.HighlightStoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/highlight-stories")
@RequiredArgsConstructor
public class HighlightStoryController {
    private final HighlightStoryService highlightStoryService;

    /**
     * Tạo Highlight Story mới.
     *
     * @param userId  ID của người dùng tạo Highlight Story.
     * @param request DTO chứa thông tin Highlight Story.
     * @return ResponseEntity chứa thông tin phản hồi.
     */
    @PostMapping("/{userId}")
    public ResponseEntity<?> createHighlightStory(
            @PathVariable String userId,
            @Valid @ModelAttribute HighlightStoryRequest request) {
        HighlightStoryResponse response = highlightStoryService.createHighlightStory(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Tạo Highlight Story thành công")
                        .data(response)
                        .build()
        );
    }

    /**
     * Lấy danh sách Highlight Story của một người dùng.
     *
     * @param userId ID của người dùng.
     * @return ResponseEntity chứa danh sách Highlight Story.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getHighlightStoriesByUser(@PathVariable String userId) {
        List<HighlightStoryResponse> stories = highlightStoryService.getHighlightStoriesByUser(userId);
        return ResponseEntity.ok(
                Response.builder()
                        .code(HttpStatus.OK.value())
                        .message("Lấy danh sách Highlight Story thành công")
                        .data(stories)
                        .build()
        );
    }


    /**
     * Lấy chi tiết của một Highlight Story.
     *
     * @param userId  ID của người dùng sở hữu Highlight Story.
     * @param storyId ID của Highlight Story.
     * @return ResponseEntity chứa thông tin chi tiết của Highlight Story.
     */
    @GetMapping("/{userId}/{storyId}")
    public ResponseEntity<?> getHighlightStoryDetail(
            @PathVariable String userId,
            @PathVariable String storyId) {
        HighlightStoryResponseDetail storyDetail = highlightStoryService.getHighlightStoryDetail(userId, storyId);
        return ResponseEntity.ok(
                Response.builder()
                        .code(HttpStatus.OK.value())
                        .message("Lấy chi tiết Highlight Story thành công")
                        .data(storyDetail)
                        .build()
        );
    }

    /**
     * Xóa Highlight Story.
     *
     * @param userId  ID của người dùng.
     * @param storyId ID của Highlight Story cần xóa.
     * @return ResponseEntity chứa thông tin phản hồi.
     */
    @DeleteMapping("/{userId}/{storyId}")
    public ResponseEntity<?> deleteHighlightStory(
            @PathVariable String userId,
            @PathVariable String storyId) {
        highlightStoryService.deleteHighlightStory(userId, storyId);
        return ResponseEntity.ok(
                Response.builder()
                        .code(HttpStatus.OK.value())
                        .message("Xóa Highlight Story thành công")
                        .data(null)
                        .build()
        );
    }

    /**
     * Thêm ảnh vào Highlight Story.
     *
     * @param userId  ID của người dùng.
     * @param storyId ID của Highlight Story.
     * @param files   Danh sách file ảnh tải lên.
     * @return ResponseEntity chứa danh sách URL ảnh đã tải lên.
     */
    @PostMapping("/{userId}/{storyId}/images")
    public ResponseEntity<?> addImagesToHighlightStory(
            @PathVariable String userId,
            @PathVariable String storyId,
            @RequestParam("files") List<MultipartFile> files) {
        List<String> uploadedUrls = highlightStoryService.addImagesToHighlightStory(userId, storyId, files);
        return ResponseEntity.ok(
                Response.builder()
                        .code(HttpStatus.OK.value())
                        .message("Thêm ảnh vào Highlight Story thành công")
                        .data(uploadedUrls)
                        .build()
        );
    }

}
