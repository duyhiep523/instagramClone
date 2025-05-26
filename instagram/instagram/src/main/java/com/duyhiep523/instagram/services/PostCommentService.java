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

    // Định nghĩa giới hạn độ sâu lồng nhau
    // Giới hạn độ sâu vẫn là 3 theo yêu cầu
    private static final int MAX_COMMENT_DEPTH = 3;

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

        // Bắt đầu với độ sâu 1 cho các bình luận gốc
        return rootComments.stream()
                .map(comment -> mapToResponseWithReplies(comment, 1))
                .collect(Collectors.toList());
    }

    /**
     * Ánh xạ một thực thể PostComment sang DTO PostCommentResponse, xử lý các bình luận trả lời lồng nhau đến độ sâu đã xác định.
     * Nếu đạt đến độ sâu tối đa, các bình luận trả lời trực tiếp vẫn được bao gồm nhưng không được xử lý đệ quy thêm.
     *
     * @param comment Thực thể PostComment để ánh xạ.
     * @param currentDepth Độ sâu đệ quy hiện tại.
     * @return DTO PostCommentResponse với các bình luận trả lời được tải theo giới hạn độ sâu.
     */
    private PostCommentResponse mapToResponseWithReplies(PostComment comment, int currentDepth) {
        PostCommentResponse response = mapToResponse(comment); // mapToResponse đã được sửa đổi để bao gồm 1 cấp con trực tiếp

        // Luôn truy xuất các bình luận trả lời trực tiếp cho bình luận hiện tại
        List<PostComment> directReplies = postCommentRepository.findByParentComment_CommentIdAndIsDeletedFalse(comment.getCommentId());

        if (currentDepth < MAX_COMMENT_DEPTH) {
            // Nếu độ sâu hiện tại nhỏ hơn độ sâu tối đa, ánh xạ đệ quy các bình luận trả lời
            response.setReplies(directReplies.stream()
                    .map(reply -> mapToResponseWithReplies(reply, currentDepth + 1)) // Đệ quy cho các bình luận trả lời sâu hơn
                    .collect(Collectors.toList()));
        } else {
            // Nếu độ sâu hiện tại đã đạt hoặc vượt quá độ sâu tối đa,
            // bao gồm các bình luận trả lời trực tiếp nhưng không đệ quy thêm.
            // mapToResponse đã tự động bao gồm 1 cấp con trực tiếp, nên không cần gọi lại ở đây.
            // Tuy nhiên, để đảm bảo tính nhất quán và rõ ràng, chúng ta vẫn sẽ thiết lập lại replies
            // nếu cần. Nhưng với logic mới của mapToResponse, phần này có thể được đơn giản hóa.
            // Logic hiện tại của mapToResponse đã bao gồm việc lấy con trực tiếp.
            // Do đó, chỉ cần đảm bảo rằng các bình luận con được lấy ra và ánh xạ đúng cách.
            response.setReplies(directReplies.stream()
                    .map(this::mapToResponse) // mapToResponse sẽ tự động lấy 1 cấp con trực tiếp
                    .collect(Collectors.toList()));
        }

        return response;
    }

    /**
     * Ánh xạ một thực thể PostComment đơn lẻ sang DTO PostCommentResponse.
     * Phương thức này hiện tại sẽ bao gồm MỘT cấp bình luận con trực tiếp,
     * nhưng không xử lý đệ quy các cấp sâu hơn của bình luận con đó.
     *
     * @param comment Thực thể PostComment để ánh xạ.
     * @return DTO PostCommentResponse.
     */
    private PostCommentResponse mapToResponse(PostComment comment) {
        // Đảm bảo thực thể người dùng được khởi tạo trước khi truy cập các thuộc tính của nó,
        // đặc biệt nếu session có thể đóng hoặc thực thể được tải lười biếng.
        Hibernate.initialize(comment.getUser());

        PostCommentResponse response = PostCommentResponse.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getPostId())
                .userId(comment.getUser().getUserId())
                .username(comment.getUser().getUsername())
                .avatarUrl(comment.getUser().getProfilePictureUrl())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .createdBy(comment.getCreatedBy())
                .isDeleted(comment.getIsDeleted())
                .build();

        // Lấy các bình luận con trực tiếp của bình luận này
        List<PostComment> directReplies = postCommentRepository.findByParentComment_CommentIdAndIsDeletedFalse(comment.getCommentId());

        // Ánh xạ các bình luận con trực tiếp này bằng một phương thức đơn giản hơn
        // không tìm nạp thêm các cấp độ sâu hơn, để tránh đệ quy vô hạn.
        response.setReplies(directReplies.stream()
                .map(this::mapToResponseSimple)
                .collect(Collectors.toList()));

        return response;
    }

    /**
     * Ánh xạ một thực thể PostComment đơn lẻ sang DTO PostCommentResponse một cách đơn giản.
     * Phương thức này KHÔNG xử lý bất kỳ bình luận trả lời lồng nhau nào và luôn đặt replies là danh sách trống.
     * Được sử dụng để "nhồi nhét" các bình luận con ở cấp độ sâu nhất.
     *
     * @param comment Thực thể PostComment để ánh xạ.
     * @return DTO PostCommentResponse.
     */
    private PostCommentResponse mapToResponseSimple(PostComment comment) {
        Hibernate.initialize(comment.getUser());
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
                .replies(List.of()) // Đảm bảo replies luôn là danh sách trống
                .build();
    }
}
