package com.duyhiep523.instagram.repositories;

import com.duyhiep523.instagram.entities.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, String> {
    List<PostComment> findByPost_PostIdAndIsDeletedFalse(String postId);
    List<PostComment> findByParentComment_CommentIdAndIsDeletedFalse(String parentCommentId);

    int countByPost_PostId(String postId);
}
