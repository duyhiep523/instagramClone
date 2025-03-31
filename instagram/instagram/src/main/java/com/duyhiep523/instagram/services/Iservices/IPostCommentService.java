package com.duyhiep523.instagram.services.Iservices;



import com.duyhiep523.instagram.dtos.request.comment.PostCommentRequest;
import com.duyhiep523.instagram.dtos.response.comment.PostCommentResponse;

import java.util.List;

public interface IPostCommentService {
    PostCommentResponse addCommentToPost(String userId, String postId, PostCommentRequest request);
    PostCommentResponse updateComment(String commentId, PostCommentRequest request);
    void deleteComment(String commentId);

    int countCommentsByPost(String postId);

    List<PostCommentResponse> getCommentsByPost(String postId);
}
