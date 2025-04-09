package com.duyhiep523.instagram.services;


import com.duyhiep523.instagram.dtos.request.comment.PostCommentRequest;
import com.duyhiep523.instagram.dtos.response.comment.PostCommentResponse;
import com.duyhiep523.instagram.entities.Post;
import com.duyhiep523.instagram.entities.PostComment;
import com.duyhiep523.instagram.entities.User;
import com.duyhiep523.instagram.exeptions.ResourceNotFoundException;
import com.duyhiep523.instagram.repositories.PostCommentRepository;
import com.duyhiep523.instagram.repositories.PostRepository;
import com.duyhiep523.instagram.repositories.UserAccountRepository;
import com.duyhiep523.instagram.services.Iservices.IPostCommentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostCommentService implements IPostCommentService {
    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final UserAccountRepository userRepository;

    @Transactional
    @Override
    public PostCommentResponse addCommentToPost(String userId, String postId, PostCommentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Bài viết không tồn tại"));

        PostComment parentComment = null;
        if (request.getParentCommentId() != null) {
            parentComment = postCommentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bình luận cha không tồn tại"));
        }

        PostComment comment = PostComment.builder()
                .post(post)
                .user(user)
                .content(request.getContent())
                .parentComment(parentComment)
                .build();

        postCommentRepository.save(comment);
        return mapToResponse(comment);
    }

    @Transactional
    @Override
    public PostCommentResponse updateComment(String commentId, PostCommentRequest request) {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Bình luận không tồn tại"));

        comment.setContent(request.getContent());
        postCommentRepository.save(comment);
        return mapToResponse(comment);
    }

    @Transactional
    @Override
    public void deleteComment(String commentId) {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Bình luận không tồn tại"));

        comment.setIsDeleted(true);
        postCommentRepository.save(comment);
    }

    @Override
    public int countCommentsByPost(String postId) {
        return postCommentRepository.countByPost_PostId(postId);
    }

    @Transactional
    @Override
    public List<PostCommentResponse> getCommentsByPost(String postId) {
        // Lấy danh sách bình luận gốc của bài viết (không có parent)
        List<PostComment> rootComments = postCommentRepository.findByPost_PostIdAndParentCommentIsNullAndIsDeletedFalse(postId);

        return rootComments.stream()
                .map(this::mapToResponseWithReplies)
                .collect(Collectors.toList());
    }

    private PostCommentResponse mapToResponseWithReplies(PostComment comment) {
        PostCommentResponse response = mapToResponse(comment);

        // Lấy danh sách bình luận con
        List<PostComment> replies = postCommentRepository.findByParentComment_CommentIdAndIsDeletedFalse(comment.getCommentId());

        // Đệ quy để lấy danh sách con
        response.setReplies(replies.stream()
                .map(this::mapToResponseWithReplies) // Đệ quy cho mỗi comment con
                .collect(Collectors.toList()));

        return response;
    }

    private PostCommentResponse mapToResponse(PostComment comment) {
        Hibernate.initialize(comment.getUser()); // Load user trước khi session đóng
        return PostCommentResponse.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getPostId())
                .userId(comment.getUser().getUserId())
                .username(comment.getUser().getUsername())
                .avatarUrl(comment.getUser().getProfilePictureUrl())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .createdBy(comment.getCreatedBy())
                .isDeleted(comment.getIsDeleted())
                .replies(comment.getReplies() != null
                        ? comment.getReplies().stream().map(this::mapToResponse).collect(Collectors.toList())
                        : List.of())
                .build();
    }

}
