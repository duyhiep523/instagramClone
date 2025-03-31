package com.duyhiep523.instagram.dtos.response.follow;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowerResponse {
    private String userId;
    private String username;
    private String fullName;
    private String profilePictureUrl;
    private String relationshipStatus;
}