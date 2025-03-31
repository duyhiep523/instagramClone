package com.duyhiep523.instagram.repositories;

import com.duyhiep523.instagram.entities.Follow;
import com.duyhiep523.instagram.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, String> {
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    boolean existsByFollowerAndFollowing(User follower, User following);


    boolean existsByFollowerUserIdAndFollowingUserId(String followerId, String followingId);

    //Lấy danh sách người theo dõi của một người dùng
    @Query("SELECT f.follower FROM Follow f WHERE f.following.userId = :userId")
    List<User> findFollowersByUserId(@Param("userId") String userId);

    //Lấy danh sách những người mà một người dùng đang theo dõi
    @Query("SELECT f.following FROM Follow f WHERE f.follower.userId = :userId")
    List<User> findFollowingByUserId(@Param("userId") String userId);


    //    Đếm số lượng người theo dõi của một người dùng
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following.userId = :userId")
    long countFollowersByUserId(@Param("userId") String userId);


    //    Đếm số lượng người mà một người dùng đang theo dõi
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.userId = :userId")
    long countFollowingByUserId(@Param("userId") String userId);



    @Query("""
    SELECT DISTINCT f2.following FROM Follow f1
    JOIN Follow f2 ON f1.following.userId = f2.follower.userId
    WHERE f1.follower.userId = :userId AND f2.following.userId <> :userId
    AND f2.following.userId NOT IN (
        SELECT f.following.userId FROM Follow f WHERE f.follower.userId = :userId
    )
    ORDER BY RAND()
""")
    List<User> findSuggestedUsers(@Param("userId") String userId);

}
