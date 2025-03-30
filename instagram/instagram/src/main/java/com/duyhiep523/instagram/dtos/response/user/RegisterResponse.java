package com.duyhiep523.instagram.dtos.response.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RegisterResponse {
    private String userId;
    private String email;
    private String username;
    private String fullName;
    private LocalDateTime createdAt;
}
