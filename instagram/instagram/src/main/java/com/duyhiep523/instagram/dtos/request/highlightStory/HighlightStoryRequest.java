package com.duyhiep523.instagram.dtos.request.highlightStory;



import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class HighlightStoryRequest {

    private String storyName;
    List<MultipartFile> images;
}
