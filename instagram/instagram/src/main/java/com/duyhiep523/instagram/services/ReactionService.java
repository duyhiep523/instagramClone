package com.duyhiep523.instagram.services;

import com.duyhiep523.instagram.entities.Post;
import com.duyhiep523.instagram.entities.PostReaction;
import com.duyhiep523.instagram.entities.User;
import com.duyhiep523.instagram.exeptions.ResourceNotFoundException;
import com.duyhiep523.instagram.repositories.PostRepository;
import com.duyhiep523.instagram.repositories.ReactionRepository;
import com.duyhiep523.instagram.repositories.UserAccountRepository;
import com.duyhiep523.instagram.services.Iservices.IReactionPostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactionService implements IReactionPostService {
    private final ReactionRepository postReactionRepository;
    private final PostRepository postRepository;
    private final UserAccountRepository userRepository;


    @Transactional
    @Override
    public void addReactionToPost(String userId, String postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Bài viết không tồn tại"));

        boolean alreadyReacted = postReactionRepository.findByUser_UserIdAndPost_PostId(userId, postId).isPresent();

        if (!alreadyReacted) {
            PostReaction reaction = PostReaction.builder()
                    .post(post)
                    .user(user)
                    .build();

            postReactionRepository.save(reaction);
        }
    }

    @Override
    public int getReactionCountByPost(String postId) {
        return postReactionRepository.countByPost_PostId(postId);
    }

    @Override
    public boolean hasUserReactedToPost(String userId, String postId) {
        return postReactionRepository.findByUser_UserIdAndPost_PostId(userId, postId).isPresent();
    }


    /**
     * Xóa cảm xúc của người dùng với bài viết
     *
     * @param userId ID của người dùng
     * @param postId ID của bài viết
     */
    @Transactional
    @Override
    public void removeReactionFromPost(String userId, String postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Bài viết không tồn tại"));
        PostReaction reaction = postReactionRepository.findByUser_UserIdAndPost_PostId(userId, postId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng chưa bày tỏ cảm xúc với bài viết này"));

        postReactionRepository.delete(reaction);
    }
//    public List<String> getReactionsByPost(String postId) {
//        List<PostReaction> reactions = postReactionRepository.findByPost_PostId(postId);
//        return reactions.stream().map(PostReaction::getReactionType).collect(Collectors.toList());
//    }


}
