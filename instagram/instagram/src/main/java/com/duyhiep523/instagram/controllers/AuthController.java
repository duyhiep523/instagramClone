package com.duyhiep523.instagram.controllers;

import com.duyhiep523.instagram.dtos.request.user.LoginRequest;
import com.duyhiep523.instagram.dtos.response.user.LoginResponse;
import com.duyhiep523.instagram.response.Response;
import com.duyhiep523.instagram.services.CustomUserDetailsService;
import com.duyhiep523.instagram.utils.JwtTokenUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${apiPrefix}/auth")
public class AuthController {


    @Value("${apiPrefix}")
    private String apiPrefix;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    /**
     * Handles user login and issues a JWT token.
     *
     * @param loginRequest DTO containing username and password.
     * @return ResponseEntity with JWT and user info on success, or error message on failure.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate the user's credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );


            UserDetails userDetails = (UserDetails) authentication.getPrincipal();


            String jwtToken = jwtTokenUtils.generateToken(userDetails);


            LoginResponse loginResponseData = LoginResponse.builder()
                    .token(jwtToken)
                    .username(userDetails.getUsername())

                    .build();

            Response<Object> response = Response.builder()
                    .code(HttpStatus.OK.value())
                    .message("Đăng nhập thành công")
                    .data(loginResponseData)
                    .build();

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            Response<Object> errorResponse = Response.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("Tên đăng nhập hoặc mật khẩu không đúng.")
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            Response<Object> errorResponse = Response.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Đăng nhập thất bại: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            Response<Object> errorResponse = Response.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("Token không hợp lệ hoặc đã hết hạn.")
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();


        LoginResponse loginResponseData = LoginResponse.builder()
                .username(userDetails.getUsername())

                .build();

        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Lấy thông tin người dùng thành công")
                .data(loginResponseData)
                .build();

        return ResponseEntity.ok(response);
    }

}