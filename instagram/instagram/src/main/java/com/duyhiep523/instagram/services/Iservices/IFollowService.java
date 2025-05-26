package com.duyhiep523.instagram.services.Iservices;

import com.duyhiep523.instagram.dtos.request.follow.FollowRequest;
import com.duyhiep523.instagram.dtos.response.follow.FollowerResponse;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IFollowService {
    void followUser(String followerId, FollowRequest request);

    @Transactional
    void unfollowUser(String followerId, String followingId);

    String getFollowStatus(String currentUserId, String targetUserId);

    List<FollowerResponse> getFollowers(String currentUserId);

    List<FollowerResponse> getFollowing(String currentUserId);

    long countFollowers(String userId);

    long countFollowing(String userId);

    List<FollowerResponse> getSuggestedUsers(String userId);

    List<FollowerResponse> getFollowersNotFollowedBack(String currentUserId);
}
