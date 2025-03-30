package com.duyhiep523.instagram.dtos.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public class LoginRequest {
    @JsonProperty("username")
    @NotBlank(message = "User name không được bỏ trống")
    private String phoneNumber;

    @NotBlank(message = "Mật khẩu không được bỏ trống")
    private String password;
}
