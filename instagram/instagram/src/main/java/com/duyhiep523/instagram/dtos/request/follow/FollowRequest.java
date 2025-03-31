package com.duyhiep523.instagram.dtos.request.follow;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class FollowRequest {
    @NotBlank(message = "ID người được theo dõi không được để trống")
    private String followingId;
}
