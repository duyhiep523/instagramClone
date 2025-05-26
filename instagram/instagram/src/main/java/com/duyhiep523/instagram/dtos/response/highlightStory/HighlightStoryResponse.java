package com.duyhiep523.instagram.dtos.response.highlightStory;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class HighlightStoryResponse {
    private String storyId;
    private String userId;
    private String storyName;
    private LocalDateTime createdAt;
    private String createdBy;
    private Boolean isDeleted;
    private String storyImage;
}