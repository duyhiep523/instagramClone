package com.duyhiep523.instagram.services;

import com.duyhiep523.instagram.common.enums.Privacy;
import com.duyhiep523.instagram.dtos.request.post.PostRequest;
import com.duyhiep523.instagram.dtos.response.post.PostResponse;
import com.duyhiep523.instagram.entities.Post;
import com.duyhiep523.instagram.entities.PostFile;
import com.duyhiep523.instagram.entities.User;
import com.duyhiep523.instagram.exeptions.BadRequestException;
import com.duyhiep523.instagram.exeptions.ResourceNotFoundException;
import com.duyhiep523.instagram.repositories.*;
import com.duyhiep523.instagram.services.Iservices.IPostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.duyhiep523.instagram.repositories.FollowRepository;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {
    private final PostRepository postRepository;
    private final PostFileRepository postFileRepository;
    private final CloudinaryService cloudinaryService;
    private final UserAccountRepository userRepository;
    private final PostCommentRepository postCommentRepository;
    private final ReactionService  postReactionRepository;
    private final FollowRepository followRepository;

    /**
     * Tạo bài viết mới
     *
     * @param userId  ID của người dùng tạo bài viết
     * @param request Thông tin bài viết
     * @return Đối tượng PostResponse chứa thông tin bài viết đã tạo
     */
    @Transactional
    @Override
    public PostResponse createPost(String userId, PostRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        if (request.getFiles() == null || request.getFiles().isEmpty()) {
            throw new BadRequestException("Bài viết phải có ít nhất một file đính kèm");
        }

        Post post = Post.builder()
                .user(user)
                .content(request.getContent())
                .privacy(Privacy.valueOf(request.getPrivacy().toUpperCase()))
                .build();

        postRepository.save(post);

        List<String> fileResponses = new ArrayList<>();
        for (MultipartFile file : request.getFiles()) {
            String fileUrl = cloudinaryService.uploadImage(file);

            PostFile postFile = PostFile.builder()
                    .post(post)
                    .fileUrl(fileUrl)
                    .build();

            postFileRepository.save(postFile);
            fileResponses.add(fileUrl);
        }

        return PostResponse.builder()
                .postId(post.getPostId())
                .userId(user.getUserId())
                .content(post.getContent())
                .privacy(post.getPrivacy().name())
                .createdAt(post.getCreatedAt())
                .fileUrls(fileResponses)
                .build();
    }

    /**
     * Lấy danh sách bài viết của người dùng
     *
     * @param userId ID của người dùng
     * @return Danh sách bài viết của người dùng
     */
    @Override
    public List<PostResponse> getPostsByUserId(String userId) {
        List<Post> posts = postRepository.findByUser_UserIdAndIsDeletedFalseOrderByCreatedAtDesc(userId);

        return posts.stream().map(post -> {
            List<String> fileUrls = postFileRepository.findByPostId(post.getPostId())
                    .stream()
                    .map(PostFile::getFileUrl)
                    .collect(Collectors.toList());

            return PostResponse.builder()
                    .postId(post.getPostId())
                    .userId(post.getUser().getUserId())
                    .content(post.getContent())
                    .privacy(String.valueOf(post.getPrivacy()))
                    .fileUrls(fileUrls)
                    .commentCount(postCommentRepository.countByPost_PostId(post.getPostId()))
                    .likeCount(postReactionRepository.getReactionCountByPost(post.getPostId()))
                    .createdAt(post.getCreatedAt())
                    .build();
        }).collect(Collectors.toList());
    }



    @Override
    public PostResponse getPostById(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Bài viết không tồn tại"));

        List<String> fileUrls = postFileRepository.findByPostId(post.getPostId())
                .stream()
                .map(PostFile::getFileUrl)
                .collect(Collectors.toList());

        return PostResponse.builder()
                .postId(post.getPostId())
                .userId(post.getUser().getUserId())
                .content(post.getContent())
                .privacy(post.getPrivacy().name())
                .fileUrls(fileUrls)
                .commentCount(postCommentRepository.countByPost_PostId(post.getPostId()))
                .likeCount(postReactionRepository.getReactionCountByPost(post.getPostId()))
                .createdAt(post.getCreatedAt())
                .build();
    }

    /**
     * Đếm số lượng bài viết của một người dùng
     *
     * @param userId ID của người dùng
     * @return Số lượng bài viết của người dùng đó
     */
    @Override
    public long countPostsByUserId(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));
        return postRepository.countByUser_UserIdAndIsDeletedFalse(userId);
    }

    @Override
    @Transactional
    public List<PostResponse> getFeedPosts(String userId, int page, int size) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));


        List<String> followingUserIds = followRepository.findFollowingByUserId(userId)
                .stream()
                .map(User::getUserId)
                .collect(Collectors.toList());


        List<String> excludeUserIds = new ArrayList<>(followingUserIds);
        excludeUserIds.add(userId);

        Pageable pageable = PageRequest.of(page, size);


        Page<Post> feedPostsPage = postRepository.findFeedPostsForUser(userId,followingUserIds, excludeUserIds, pageable);

        return feedPostsPage.getContent().stream().map(post -> {
            List<String> fileUrls = postFileRepository.findByPostId(post.getPostId())
                    .stream()
                    .map(PostFile::getFileUrl)
                    .collect(Collectors.toList());

            return PostResponse.builder()
                    .postId(post.getPostId())
                    .userId(post.getUser().getUserId())
                    .content(post.getContent())
                    .privacy(String.valueOf(post.getPrivacy()))
                    .fileUrls(fileUrls)
                    .commentCount(postCommentRepository.countByPost_PostId(post.getPostId()))
                    .likeCount(postReactionRepository.getReactionCountByPost(post.getPostId()))
                    .createdAt(post.getCreatedAt())
                    .build();
        }).collect(Collectors.toList());
    }



    @Transactional
    @Override
    public PostResponse updatePost(String userId, String postId, PostRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Bài viết không tồn tại"));

        if (!post.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Bạn không có quyền chỉnh sửa bài viết này");
        }
        if (request.getContent() != null && !request.getContent().trim().isEmpty()) {
            post.setContent(request.getContent());
        }
        if (request.getPrivacy() != null) {
            post.setPrivacy(Privacy.valueOf(request.getPrivacy().toUpperCase()));
        }

        // Nếu có file mới -> xóa toàn bộ file cũ + thêm mới
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            List<PostFile> oldFiles = postFileRepository.findByPostId(postId);
            postFileRepository.deleteAll(oldFiles);

            // Upload file mới
            List<PostFile> newFiles = new ArrayList<>();
            for (MultipartFile file : request.getFiles()) {
                String fileUrl = cloudinaryService.uploadImage(file);

                PostFile postFile = PostFile.builder()
                        .post(post)
                        .fileUrl(fileUrl)
                        .build();

                newFiles.add(postFile);
            }
            postFileRepository.saveAll(newFiles);
        }

        postRepository.save(post);

        // Trả về response
        List<String> fileUrls = postFileRepository.findByPostId(post.getPostId())
                .stream()
                .map(PostFile::getFileUrl)
                .collect(Collectors.toList());

        return PostResponse.builder()
                .postId(post.getPostId())
                .userId(post.getUser().getUserId())
                .content(post.getContent())
                .privacy(post.getPrivacy().name())
                .fileUrls(fileUrls)
                .commentCount(postCommentRepository.countByPost_PostId(post.getPostId()))
                .likeCount(postReactionRepository.getReactionCountByPost(post.getPostId()))
                .createdAt(post.getCreatedAt())
                .build();
    }


}