package com.duyhiep523.instagram.services;

import com.duyhiep523.instagram.dtos.request.follow.FollowRequest;
import com.duyhiep523.instagram.dtos.response.follow.FollowerResponse;
import com.duyhiep523.instagram.entities.Follow;
import com.duyhiep523.instagram.entities.User;
import com.duyhiep523.instagram.exeptions.BadRequestException;
import com.duyhiep523.instagram.exeptions.ConflictException;
import com.duyhiep523.instagram.exeptions.ResourceNotFoundException;
import com.duyhiep523.instagram.repositories.FollowRepository;
import com.duyhiep523.instagram.repositories.UserAccountRepository;
import com.duyhiep523.instagram.services.Iservices.IFollowService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService implements IFollowService {
    private final FollowRepository followRepository;
    private final UserAccountRepository userRepository;

    /**
     * Follow a user
     *
     * @param followerId ID of the follower
     * @param request    FollowRequest containing the ID of the user to follow
     */
    @Transactional
    @Override
    public void followUser(String followerId, FollowRequest request) {

        if (followerId.equals(request.getFollowingId())) {
            throw new BadRequestException("Bạn không thể theo dõi chính mình");
        }
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Người theo dõi không tồn tại"));

        User following = userRepository.findById(request.getFollowingId())
                .orElseThrow(() -> new ResourceNotFoundException("Người được theo dõi không tồn tại"));

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new ConflictException("Bạn đã theo dõi người dùng này rồi");
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        followRepository.save(follow);
    }

    /**
     * Unfollow a user
     *
     * @param followerId  ID of the follower
     * @param followingId ID of the user to unfollow
     */
    @Transactional
    @Override
    public void unfollowUser(String followerId, String followingId) {

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Người theo dõi không tồn tại"));


        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new ResourceNotFoundException("Người được theo dõi không tồn tại"));


        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new BadRequestException("Bạn chưa theo dõi người dùng này"));


        followRepository.delete(follow);
    }

    /**
     * Get the follow status between two users
     *
     * @param currentUserId ID of the current user
     * @param targetUserId  ID of the target user
     * @return Follow status as a string
     */
    @Override
    public String getFollowStatus(String currentUserId, String targetUserId) {
        boolean isFollowing = followRepository.existsByFollowerUserIdAndFollowingUserId(currentUserId, targetUserId);
        boolean isFollowedBack = followRepository.existsByFollowerUserIdAndFollowingUserId(targetUserId, currentUserId);

        if (isFollowing && isFollowedBack) {
            return "friend"; // Cả hai đều theo dõi nhau → bạn bè
        } else if (isFollowing) {
            return "following"; // Mình theo dõi người kia nhưng họ không theo dõi lại
        } else if (isFollowedBack) {
            return "followed_back"; // Người kia theo dõi mình nhưng mình không theo dõi lại
        }
        return "none"; // Không có mối quan hệ nào
    }

    /**
     * Get the list of followers for a user
     *
     * @param currentUserId ID of the current user
     * @return List of FollowerResponse containing follower details
     */
    @Override
    public List<FollowerResponse> getFollowers(String currentUserId) {
        List<User> followers = followRepository.findFollowersByUserId(currentUserId);

        return followers.stream().map(user -> FollowerResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .relationshipStatus(getFollowStatus(currentUserId, user.getUserId()))
                .build()).collect(Collectors.toList());
    }


    /**
     * Get the list of users that the current user is following
     *
     * @param currentUserId ID of the current user
     * @return List of FollowerResponse containing following details
     */
    @Override
    public List<FollowerResponse> getFollowing(String currentUserId) {
        List<User> followingUsers = followRepository.findFollowingByUserId(currentUserId);

        return followingUsers.stream().map(user -> FollowerResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .relationshipStatus(getFollowStatus(currentUserId, user.getUserId()))
                .build()).collect(Collectors.toList());
    }

    /**
     * Count the number of followers for a user
     *
     * @param userId ID of the user
     * @return Number of followers
     */
    @Override
    public long countFollowers(String userId) {
        return followRepository.countFollowersByUserId(userId);
    }

    /**
     * Count the number of users that a user is following
     *
     * @param userId ID of the user
     * @return Number of users being followed
     */
    @Override
    public long countFollowing(String userId) {
        return followRepository.countFollowingByUserId(userId);
    }

    /**
     * Get a list of suggested users for a user
     *
     * @param userId ID of the user
     * @return List of FollowerResponse containing suggested user details
     */
    @Override
    public List<FollowerResponse> getSuggestedUsers(String userId) {
        List<User> suggestedUsers = followRepository.findSuggestedUsers(userId);

        return suggestedUsers.stream().map(user -> FollowerResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .relationshipStatus(getFollowStatus(userId, user.getUserId()))
                .build()).collect(Collectors.toList());
    }

}
