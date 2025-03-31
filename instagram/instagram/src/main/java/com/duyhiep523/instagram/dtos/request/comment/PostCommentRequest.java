package com.duyhiep523.instagram.dtos.request.comment;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PostCommentRequest {
    @NotBlank(message = "Nội dung bình luận không được để trống")
    private String content;
    private String parentCommentId;
}
