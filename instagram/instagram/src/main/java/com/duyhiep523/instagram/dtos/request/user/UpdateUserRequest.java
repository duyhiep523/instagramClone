package com.duyhiep523.instagram.dtos.request.user;

import com.duyhiep523.instagram.annotation.ValidEnum;
import com.duyhiep523.instagram.common.enums.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    @NotBlank(message = "Username không được để trống")
    @Size(min = 1, max = 50, message = "Username phải có độ dài từ 1 đến 50 ký tự")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên không được vượt quá 100 ký tự")
    private String fullName;

    @Size(max = 255, message = "Tiểu sử không được vượt quá 255 ký tự")
    private String bio;

    @ValidEnum(enumClass = Gender.class,message = "Giới tính không hợp lệ")
    private String gender;
}
