package com.duyhiep523.instagram.dtos.response.comment;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Data
public class PostCommentResponse {
    private String commentId;
    private String postId;
    private String userId;
    private String username;
    private String avatarUrl;
    private String content;
    private LocalDateTime createdAt;
    private String createdBy;
    private boolean isDeleted;
    private List<PostCommentResponse> replies;
}
