package com.duyhiep523.instagram.dtos.response.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private String postId;
    private String content;
    private String privacy;
    private List<String> fileUrls;
    private LocalDateTime createdAt;
}
