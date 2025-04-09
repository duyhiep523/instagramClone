package com.duyhiep523.instagram.dtos.response.highlightStory;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HighlightStoryResponseDetail {
    private String storyId;
    private String storyName;
    private String createdAt;
    private String updatedAt;
    private Boolean isDeleted;

    // Thông tin người tạo
    private String username;
    private String avatarUrl;

    // Danh sách ảnh
    private List<String> imageUrls;
}
