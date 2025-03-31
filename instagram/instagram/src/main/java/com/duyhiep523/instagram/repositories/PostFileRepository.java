package com.duyhiep523.instagram.repositories;

import com.duyhiep523.instagram.entities.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostFileRepository extends JpaRepository<PostFile, String> {


    @Query("SELECT pf FROM PostFile pf WHERE pf.post.postId = :postId")
    List<PostFile> findByPostId(@Param("postId") String postId);
}