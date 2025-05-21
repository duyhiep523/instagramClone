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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class UserService implements IUserService {
    @Autowired
    UserAccountRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired // <-- TIÊM PASSWORD ENCODER VÀO ĐÂY
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getUserName());

        if (userRepository.findByUsername(request.getUserName()).isPresent()) {
            log.warn("Username {} already exists", request.getUserName());
            throw new ConflictException("Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Email {} already exists", request.getEmail());
            throw new ConflictException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUserName())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        user = userRepository.save(user);
        log.info("User {} registered successfully with ID: {}", user.getUsername(), user.getUserId());

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
//        log.info("User {} attempting to login", loginRequest.getUsername());
        // Implement login logic here
        return null;
    }

    @Transactional
    @Override
    public UserResponse updateAvatar(String userId, MultipartFile file) {
        log.info("Updating avatar for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", userId);
                    return new ResourceNotFoundException("User not found");
                });

        String avatarUrl = cloudinaryService.uploadImage(file);
        user.setProfilePictureUrl(avatarUrl);
        user = userRepository.save(user);

        log.info("Avatar updated successfully for user ID: {}", userId);

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

    @Override
    @Transactional
    public UserResponse updateUserInfo(String userId, UpdateUserRequest request) {
        log.info("Updating user info for ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", userId);
                    return new ResourceNotFoundException("User not found");
                });

        if (!user.getUsername().equals(request.getUsername()) &&
                userRepository.findByUsername(request.getUsername()).isPresent()) {
            log.warn("Username {} is already taken", request.getUsername());
            throw new ConflictException("Username already exists");
        }
        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Email {} is already taken", request.getEmail());
            throw new ConflictException("Email already exists");
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setBio(request.getBio());
        user.setGender(Gender.valueOf(request.getGender()));

        userRepository.save(user);
        log.info("User info updated successfully for ID: {}", userId);

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

    @Override
    public UserResponse getUserDetail(String userId) {
        log.info("Fetching user details for ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", userId);
                    return new ResourceNotFoundException("Người dùng không tồn tại");
                });

        log.info("User details retrieved successfully for ID: {}", userId);
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

    @Override
    public List<UserResponse> searchUsers(String keyword) {
        log.info("Searching users with keyword: {}", keyword);

        List<User> users = userRepository.findByUsernameContainingOrEmailContaining(keyword, keyword);

        log.info("Found {} users for keyword {}", users.size(), keyword);

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

    @Transactional
    @Override
    public void changePassword(String userId, ChangePasswordRequest request) {
        log.info("Changing password for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", userId);
                    return new ResourceNotFoundException("Không tìm thấy người dùng");
                });

        if (!request.getOldPassword().equals(user.getPassword())) {
            log.warn("Incorrect old password for user ID: {}", userId);
            throw new ConflictException("Mật khẩu cũ không đúng");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            log.warn("Password confirmation does not match for user ID: {}", userId);
            throw new ConflictException("Xác nhận mật khẩu không khớp");
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        log.info("Password changed successfully for user ID: {}", userId);
    }

    @Override
    public UserResponse getUserDetailByUsername(String username) {
        log.info("Fetching user details for username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User with username {} not found", username);
                    return new ResourceNotFoundException("Người dùng không tồn tại");
                });

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
}

