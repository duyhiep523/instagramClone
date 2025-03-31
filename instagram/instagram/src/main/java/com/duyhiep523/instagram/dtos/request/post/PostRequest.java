package com.duyhiep523.instagram.dtos.request.post;

import com.duyhiep523.instagram.annotation.ValidEnum;
import com.duyhiep523.instagram.common.enums.Privacy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    private String content;
    @ValidEnum(enumClass = Privacy.class, message = "Invalid privacy value")
    private String privacy;

    private List<MultipartFile> files;
}