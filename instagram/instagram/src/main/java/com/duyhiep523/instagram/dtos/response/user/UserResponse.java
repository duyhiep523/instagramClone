package com.duyhiep523.instagram.dtos.response.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class UserResponse {
    private String userId;
    private String username;
    private String email;
    private String fullName;
    private String profilePictureUrl;
    private String bio;
    private String gender;
    private String hometown;
    private LocalDateTime dateOfBirth;

}
