package com.duyhiep523.instagram.dtos.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
@Setter
@Getter
public class RegisterRequest {
    @JsonProperty("email")
    @NotBlank(message = "Email không được bỏ trống")
    private String email;
    @JsonProperty("username")
    @NotBlank(message = "Tên người dùng không được bỏ trống")
    private String userName;
    @JsonProperty("password")
    @NotBlank(message = "Mật khẩu không được bỏ trống")
    private String password;
    @JsonProperty("full_name")
    @NotBlank(message = "Họ và tên không được bỏ trống")
    private String fullName;
}
