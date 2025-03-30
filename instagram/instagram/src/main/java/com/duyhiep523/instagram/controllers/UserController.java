package com.duyhiep523.instagram.controllers;

import com.duyhiep523.instagram.dtos.request.user.ChangePasswordRequest;
import com.duyhiep523.instagram.dtos.request.user.RegisterRequest;
import com.duyhiep523.instagram.dtos.request.user.UpdateUserRequest;
import com.duyhiep523.instagram.dtos.response.user.RegisterResponse;
import com.duyhiep523.instagram.dtos.response.user.UserResponse;
import com.duyhiep523.instagram.response.Response;
import com.duyhiep523.instagram.services.Iservices.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping(path = "${apiPrefix}/users")
public class UserController {
    @Autowired
    private IUserService iUserService;

    /**
     * Đăng ký người dùng mới.
     *
     * @param userDTO DTO chứa thông tin đăng ký.
     * @return ResponseEntity chứa thông tin phản hồi.
     */
    @PostMapping(path = "/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest userDTO) {

        RegisterResponse registeredUser = iUserService.register(userDTO);
        Response<Object> response = Response.builder().code(HttpStatus.CREATED.value()).message("Đăng ký người dùng thành công").data(registeredUser).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/{userId}/avatar")
    public ResponseEntity<?> updateAvatar(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
        UserResponse updatedUser = iUserService.updateAvatar(userId, file);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Cập nhật ảnh đại diện thành công")
                .data(updatedUser)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Cập nhật thông tin người dùng.
     *
     * @param userId  ID của người dùng.
     * @param request DTO chứa thông tin cập nhật.
     * @return ResponseEntity chứa thông tin phản hồi.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable String userId,
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponse updatedUser = iUserService.updateUserInfo(userId, request);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Cập nhật thông tin người dùng thành công")
                .data(updatedUser)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Lấy thông tin chi tiết của người dùng.
     *
     * @param userId ID của người dùng.
     * @return ResponseEntity chứa thông tin phản hồi.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserDetail(@PathVariable String userId) {
        UserResponse userDetail = iUserService.getUserDetail(userId);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Lấy thông tin người dùng thành công")
                .data(userDetail)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Tìm kiếm người dùng theo từ khóa.
     *
     * @param keyword Từ khóa tìm kiếm.
     * @return ResponseEntity chứa danh sách người dùng tìm thấy.
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam String keyword) {
        List<UserResponse> users = iUserService.searchUsers(keyword);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Tìm kiếm thành công")
                .data(users)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Đổi mật khẩu của người dùng.
     *
     * @param userId  ID của người dùng.
     * @param request DTO chứa thông tin đổi mật khẩu.
     * @return ResponseEntity chứa thông tin phản hồi.
     */
    @PostMapping("/{userId}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable String userId,
                                            @Valid @RequestBody ChangePasswordRequest request) {
        iUserService.changePassword(userId, request);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Đổi mật khẩu thành công")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }

}
