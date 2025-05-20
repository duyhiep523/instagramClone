package com.duyhiep523.instagram.dtos.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // <-- THÊM IMPORT NÀY
import lombok.AllArgsConstructor;
@Builder
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @JsonProperty("username")
    @NotBlank(message = "User name không được bỏ trống")
    private String username;

    @NotBlank(message = "Mật khẩu không được bỏ trống")
    private String password;
}
