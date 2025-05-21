package com.duyhiep523.instagram.repositories;

import com.duyhiep523.instagram.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByUser_UserIdAndIsDeletedFalse(String userId);
    long countByUser_UserIdAndIsDeletedFalse(String userId);
    @Query("SELECT p FROM Post p " +
            "WHERE p.isDeleted = FALSE AND " +
            "(" +
            "   p.user.userId = :currentUserId OR " + // <-- THÊM DÒNG NÀY: Bao gồm bài viết của chính người dùng
            "   (p.user.userId IN :followingUserIds AND (p.privacy = 'PUBLIC' OR p.privacy = 'FRIENDS')) OR " + // Bài viết từ người dùng đang theo dõi
            "   (p.privacy = 'PUBLIC' AND p.user.userId NOT IN :excludeUserIdsForDiscovery) " + // Bài viết công khai từ người khác (loại trừ mình và người theo dõi)
            ") " +
            "ORDER BY p.createdAt DESC")
    Page<Post> findFeedPostsForUser(
            @Param("currentUserId") String currentUserId, // <-- THÊM THAM SỐ NÀY
            @Param("followingUserIds") List<String> followingUserIds,
            @Param("excludeUserIdsForDiscovery") List<String> excludeUserIdsForDiscovery,
            Pageable pageable
    );

}