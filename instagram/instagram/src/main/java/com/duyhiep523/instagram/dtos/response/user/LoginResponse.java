package com.duyhiep523.instagram.dtos.response.user;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LoginResponse {
    private String token;
    private String username;
}
