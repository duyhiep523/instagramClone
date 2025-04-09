package com.duyhiep523.instagram.repositories;


import com.duyhiep523.instagram.entities.HighlightStory;
import com.duyhiep523.instagram.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HighlightStoryRepository extends JpaRepository<HighlightStory, String> {
    List<HighlightStory> findByUser_UserIdAndIsDeletedFalse(String userId);

    List<HighlightStory> findByUserAndIsDeletedFalse(User user);

    Optional<HighlightStory> findByStoryIdAndUser_UserId (String storyId, String userId);
}
