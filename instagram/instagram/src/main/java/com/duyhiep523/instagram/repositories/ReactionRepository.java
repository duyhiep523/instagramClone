package com.duyhiep523.instagram.repositories;

import com.duyhiep523.instagram.entities.Post;
import com.duyhiep523.instagram.entities.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<PostReaction, String> {

    Optional<PostReaction> findByUser_UserIdAndPost_PostId(String userId, String postId);

    int countByPost_PostId(String postId);

//    List<PostReaction> findByPost_PostId(String postId);
}
