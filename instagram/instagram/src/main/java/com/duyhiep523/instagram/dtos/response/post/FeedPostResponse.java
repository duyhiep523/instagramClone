package com.duyhiep523.instagram.dtos.response.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedPostResponse {
    private String postId;
    private String content;
    private List<String> fileUrls;
    private LocalDateTime createdAt;
    private int likeCount;
    private int commentCount;
    private UserInfo user;
    private boolean likedByCurrentUser;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfo {
        private String userId;
        private String username;
        private String fullName;
        private String profilePictureUrl;
    }
}
