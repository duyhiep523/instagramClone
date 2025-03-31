package com.duyhiep523.instagram.services.Iservices;

import com.duyhiep523.instagram.dtos.request.post.PostRequest;
import com.duyhiep523.instagram.dtos.response.post.PostResponse;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPostService {

    PostResponse createPost(String userId, PostRequest request);

    List<PostResponse> getPostsByUserId(String userId);

    PostResponse getPostById(String postId);
}
