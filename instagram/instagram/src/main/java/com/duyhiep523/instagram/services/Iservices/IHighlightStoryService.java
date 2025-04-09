package com.duyhiep523.instagram.services.Iservices;


import com.duyhiep523.instagram.dtos.request.highlightStory.HighlightStoryRequest;
import com.duyhiep523.instagram.dtos.response.highlightStory.HighlightStoryResponse;
import com.duyhiep523.instagram.dtos.response.highlightStory.HighlightStoryResponseDetail;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IHighlightStoryService {
    HighlightStoryResponse createHighlightStory(String userId, HighlightStoryRequest request);

    @Transactional
    HighlightStoryResponseDetail getHighlightStoryDetail(String userId, String storyId);

    List<HighlightStoryResponse> getHighlightStoriesByUser(String userId);

    @Transactional
    void deleteHighlightStory(String userId, String storyId);
}
