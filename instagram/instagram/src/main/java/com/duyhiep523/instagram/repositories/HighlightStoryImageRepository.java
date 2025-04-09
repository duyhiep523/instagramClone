package com.duyhiep523.instagram.repositories;

import com.duyhiep523.instagram.entities.HighlightStoryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HighlightStoryImageRepository extends JpaRepository<HighlightStoryImage, String> {
    List<HighlightStoryImage> findByHighlightStory_StoryIdAndIsDeletedFalse(String storyId);
}
