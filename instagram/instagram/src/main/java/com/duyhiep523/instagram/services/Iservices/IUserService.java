package com.duyhiep523.instagram.services.Iservices;

import com.duyhiep523.instagram.dtos.request.user.ChangePasswordRequest;
import com.duyhiep523.instagram.dtos.request.user.LoginRequest;
import com.duyhiep523.instagram.dtos.request.user.RegisterRequest;
import com.duyhiep523.instagram.dtos.request.user.UpdateUserRequest;
import com.duyhiep523.instagram.dtos.response.user.LoginResponse;
import com.duyhiep523.instagram.dtos.response.user.RegisterResponse;
import com.duyhiep523.instagram.dtos.response.user.UserResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest loginRequest);
    UserResponse updateAvatar(String userId, MultipartFile file);
    UserResponse updateUserInfo(String userId, UpdateUserRequest request);

    UserResponse getUserDetail(String userId);

    List<UserResponse> searchUsers(String keyword);

    void changePassword(String userId, ChangePasswordRequest request);

    UserResponse getUserDetailByUsername(String username);
}
