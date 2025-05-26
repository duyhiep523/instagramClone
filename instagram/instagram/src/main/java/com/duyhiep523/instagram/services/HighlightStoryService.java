package com.duyhiep523.instagram.services;


import com.duyhiep523.instagram.dtos.request.highlightStory.HighlightStoryRequest;
import com.duyhiep523.instagram.dtos.response.highlightStory.HighlightStoryResponse;
import com.duyhiep523.instagram.dtos.response.highlightStory.HighlightStoryResponseDetail;
import com.duyhiep523.instagram.entities.HighlightStory;
import com.duyhiep523.instagram.entities.HighlightStoryImage;
import com.duyhiep523.instagram.entities.User;
import com.duyhiep523.instagram.exeptions.BadRequestException;
import com.duyhiep523.instagram.exeptions.ResourceNotFoundException;
import com.duyhiep523.instagram.exeptions.UnauthorizedException;
import com.duyhiep523.instagram.repositories.HighlightStoryImageRepository;
import com.duyhiep523.instagram.repositories.HighlightStoryRepository;
import com.duyhiep523.instagram.repositories.UserAccountRepository;
import com.duyhiep523.instagram.services.Iservices.IHighlightStoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HighlightStoryService implements IHighlightStoryService {
    private final HighlightStoryRepository highlightStoryRepository;
    private final UserAccountRepository userRepository;
    private final HighlightStoryImageRepository highlightStoryImageRepository;
    private final CloudinaryService cloudinaryService;
    /**
     * Tạo Highlight Story mới
     *
     * @param userId  ID của người dùng tạo Highlight Story
     * @param request Thông tin Highlight Story
     * @return Đối tượng HighlightStoryResponse chứa thông tin Highlight Story đã tạo
     */
    @Transactional
    @Override
    public HighlightStoryResponse createHighlightStory(String userId, HighlightStoryRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        if (request.getImages() == null || request.getImages().isEmpty()) {
            throw new BadRequestException("Cần ít nhất một ảnh để tạo Highlight Story");
        }

        // Tạo Highlight Story
        HighlightStory story = HighlightStory.builder()
                .user(user)
                .storyName(request.getStoryName())
                .build();

        highlightStoryRepository.save(story);

        // Danh sách ảnh đã tải lên
        List<HighlightStoryImage> images = new ArrayList<>();

        // Tải ảnh lên và lưu vào database
        for (MultipartFile file : request.getImages()) {
            String imageUrl = cloudinaryService.uploadImage(file);

            HighlightStoryImage image = HighlightStoryImage.builder()
                    .highlightStory(story)
                    .imageUrl(imageUrl)
                    .build();

            images.add(image);
        }

        // Lưu danh sách ảnh vào database
        highlightStoryImageRepository.saveAll(images);

        return mapToResponse(story);
    }


    /**
     * Lấy chi tiết của một Highlight Story
     *
     * @param userId  ID của người dùng sở hữu Highlight Story
     * @param storyId ID của Highlight Story
     * @return Đối tượng HighlightStoryResponseDetail chứa thông tin chi tiết của Highlight Story
     */
    @Transactional
    @Override
    public HighlightStoryResponseDetail getHighlightStoryDetail(String userId, String storyId) {
        HighlightStory story = highlightStoryRepository.findByStoryIdAndUser_UserId (storyId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Highlight Story không tồn tại hoặc không thuộc về người dùng"));

        User user = story.getUser();
        List<String> imageUrls = story.getImages().stream()
                .map(HighlightStoryImage::getImageUrl)
                .collect(Collectors.toList());

        return HighlightStoryResponseDetail.builder()
                .storyId(story.getStoryId())
                .storyName(story.getStoryName())
                .createdAt(story.getCreatedAt().toString())
                .updatedAt(story.getUpdatedAt().toString())
                .isDeleted(story.getIsDeleted())
                .username(user.getUsername())
                .avatarUrl(user.getProfilePictureUrl())
                .imageUrls(imageUrls)
                .build();
    }



    public String getFirstImageOfStory(HighlightStory story) {
        return highlightStoryImageRepository
                .findFirstByHighlightStoryOrderByCreatedAtAsc(story)
                .map(HighlightStoryImage::getImageUrl)
                .orElse(null);}

//    @Override
//    public List<HighlightStoryResponse> getHighlightStoriesByUser(String userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));
//
//        List<HighlightStory> stories = highlightStoryRepository.findByUserAndIsDeletedFalse(user);
//
//        return stories.stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }

    @Override
    public List<HighlightStoryResponse> getHighlightStoriesByUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        List<HighlightStory> stories = highlightStoryRepository.findByUserAndIsDeletedFalse(user);

        return stories.stream()
                .map(story -> {
                    String imageUrl = getFirstImageOfStory(story);

                    return HighlightStoryResponse.builder()
                            .storyId(story.getStoryId())
                            .userId(story.getUser().getUserId())
                            .storyName(story.getStoryName())
                            .createdAt(story.getCreatedAt())
                            .createdBy(story.getCreatedBy())
                            .isDeleted(story.getIsDeleted())
                            .storyImage(imageUrl)
                            .build();
                })
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public void deleteHighlightStory(String userId, String storyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        HighlightStory story = highlightStoryRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Highlight Story không tồn tại"));

        if (!story.getUser().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedException("Bạn không có quyền xóa highlight story này");
        }

        story.setIsDeleted(true);
        highlightStoryRepository.save(story);
    }

    private HighlightStoryResponse mapToResponse(HighlightStory story) {
        return HighlightStoryResponse.builder()
                .storyId(story.getStoryId())
                .userId(story.getUser().getUserId())
                .storyName(story.getStoryName())
                .createdAt(story.getCreatedAt())
                .createdBy(story.getCreatedBy())
                .isDeleted(story.getIsDeleted())
                .build();
    }


    /**
     * Thêm ảnh vào Highlight Story
     *
     * @param userId    ID của người dùng
     * @param storyId   ID của Highlight Story
     * @param files     Danh sách file ảnh tải lên
     */
    @Transactional
    public List<String> addImagesToHighlightStory(String userId, String storyId, List<MultipartFile> files) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        HighlightStory story = highlightStoryRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Highlight Story không tồn tại"));

        if (!story.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Bạn không có quyền thêm ảnh vào Highlight Story này");
        }

        if (files == null || files.isEmpty()) {
            throw new BadRequestException("Cần ít nhất một ảnh để thêm vào Highlight Story");
        }

        List<String> uploadedUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String imageUrl = cloudinaryService.uploadImage(file);

            HighlightStoryImage image = HighlightStoryImage.builder()
                    .highlightStory(story)
                    .imageUrl(imageUrl)
                    .build();

            highlightStoryImageRepository.save(image);
            uploadedUrls.add(imageUrl);
        }

        return uploadedUrls;
    }



    @Transactional
    @Override
    public HighlightStoryResponse updateHighlightStory(String userId, String storyId, HighlightStoryRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));
        HighlightStory story = highlightStoryRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Highlight Story không tồn tại"));
        if (!story.getUser().getUserId().equals(userId)) {
            throw new UnauthorizedException("Bạn không có quyền cập nhật highlight story này");
        }
        if (request.getStoryName() != null && !request.getStoryName().trim().isEmpty()) {
            story.setStoryName(request.getStoryName());
        }
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            Iterator<HighlightStoryImage> iterator = story.getImages().iterator();
            while (iterator.hasNext()) {
                HighlightStoryImage image = iterator.next();
                iterator.remove();
            }
            for (MultipartFile file : request.getImages()) {
                String imageUrl = cloudinaryService.uploadImage(file);
                HighlightStoryImage newImage = HighlightStoryImage.builder()
                        .highlightStory(story)
                        .imageUrl(imageUrl)
                        .build();
                story.getImages().add(newImage);
            }

        } else {
//            // Nếu không có ảnh mới, xóa toàn bộ ảnh cũ
//            Iterator<HighlightStoryImage> iterator = story.getImages().iterator();
//            while (iterator.hasNext()) {
//                HighlightStoryImage image = iterator.next();
//                // cloudinaryService.deleteImage(image.getImageUrl()); // Nếu cần
//                iterator.remove();
//            }
             throw new BadRequestException("Phải cung cấp ít nhất một ảnh mới");
        }
        return mapToResponse(story);
    }

}
