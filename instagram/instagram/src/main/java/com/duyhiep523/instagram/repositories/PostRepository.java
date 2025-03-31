package com.duyhiep523.instagram.repositories;

import com.duyhiep523.instagram.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {

    List<Post> findByUser_UserIdAndIsDeletedFalse(String userId);


}