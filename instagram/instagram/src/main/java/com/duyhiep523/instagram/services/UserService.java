package com.duyhiep523.instagram.services;

import com.duyhiep523.instagram.common.enums.Gender;
import com.duyhiep523.instagram.dtos.request.user.ChangePasswordRequest;
import com.duyhiep523.instagram.dtos.request.user.LoginRequest;
import com.duyhiep523.instagram.dtos.request.user.RegisterRequest;
import com.duyhiep523.instagram.dtos.request.user.UpdateUserRequest;
import com.duyhiep523.instagram.dtos.response.user.LoginResponse;
import com.duyhiep523.instagram.dtos.response.user.RegisterResponse;
import com.duyhiep523.instagram.dtos.response.user.UserResponse;
import com.duyhiep523.instagram.entities.User;
import com.duyhiep523.instagram.exeptions.ConflictException;
import com.duyhiep523.instagram.exeptions.ResourceNotFoundException;
import com.duyhiep523.instagram.repositories.UserAccountRepository;
import com.duyhiep523.instagram.services.Iservices.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    UserAccountRepository userRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    /**
     * Đăng ký người dùng mới.
     *
     * @param request DTO chứa thông tin đăng ký.
     * @return ResponseEntity chứa thông tin phản hồi.
     */

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUserName()).isPresent()) {
            throw new ConflictException("Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("Email already exists");
        }
        User user = User.builder()
                .username(request.getUserName())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(request.getPassword())
                .build();
        user = userRepository.save(user);
        return RegisterResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // Implement login logic here
        return null;
    }

    /**
     * Cập nhật ảnh đại diện của người dùng.
     *
     * @param userId ID người dùng cần cập nhật ảnh đại diện.
     * @param file   Ảnh đại diện mới.
     * @return Response chứa thông tin user sau khi cập nhật.
     */
    @Transactional
    @Override
    public UserResponse updateAvatar(String userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String avatarUrl = cloudinaryService.uploadImage(file);
        user.setProfilePictureUrl(avatarUrl);
        user = userRepository.save(user);

        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .bio(user.getBio())
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }

    /**
     * Cập nhật thông tin người dùng.
     *
     * @param userId  ID người dùng cần cập nhật.
     * @param request DTO chứa thông tin cập nhật.
     * @return Response chứa thông tin user sau khi cập nhật.
     */
    @Override
    @Transactional
    public UserResponse updateUserInfo(String userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getUsername().equals(request.getUsername()) &&
                userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ConflictException("Username already exists");
        }
        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("Email already exists");
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setBio(request.getBio());
        user.setGender(Gender.valueOf(request.getGender()));
        userRepository.save(user);

        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .bio(user.getBio())
                .gender(user.getGender().toString())
                .hometown(user.getHometown())
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }


    /**
     * Lấy thông tin chi tiết của người dùng.
     *
     * @param userId ID người dùng cần lấy thông tin.
     * @return Response chứa thông tin chi tiết của người dùng.
     */
    @Override
    public UserResponse getUserDetail(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .bio(user.getBio())
                .gender(user.getGender().name())
                .hometown(user.getHometown())
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }

    /**
     * Tìm kiếm người dùng theo tên người dùng hoặc email.
     *
     * @param keyword Từ khóa tìm kiếm.
     * @return Danh sách người dùng tìm thấy.
     */
    @Override
    public List<UserResponse> searchUsers(String keyword) {
        List<User> users = userRepository.findByUsernameContainingOrEmailContaining(keyword, keyword);

        return users.stream().map(user -> UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .bio(user.getBio())
                .gender(user.getGender().name())
                .hometown(user.getHometown())
                .dateOfBirth(user.getDateOfBirth())
                .build()).collect(Collectors.toList());
    }

    /**
     * Thay đổi mật khẩu của người dùng.
     *
     * @param userId  ID người dùng cần thay đổi mật khẩu.
     * @param request DTO chứa thông tin thay đổi mật khẩu.
     */
    @Transactional
    @Override
    public void changePassword(String userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        if (!request.getOldPassword().equals(user.getPassword())) {
            throw new ConflictException("Mật khẩu cũ không đúng");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ConflictException("Xác nhận mật khẩu không khớp");
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }

}
