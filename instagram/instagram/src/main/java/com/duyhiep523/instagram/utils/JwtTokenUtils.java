// Đường dẫn file: src/main/java/com/duyhiep523/instagram/utils/JwtTokenUtils.java
package com.duyhiep523.instagram.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JwtTokenUtils là một lớp tiện ích để tạo, phân tích và xác thực JSON Web Tokens (JWTs).
 */
@Component // Đánh dấu đây là một Spring Component để Spring có thể tự động quản lý
public class JwtTokenUtils {

    // Khóa bí mật dùng để ký (sign) và xác thực JWT
    // Nên được lưu trữ an toàn, ví dụ trong biến môi trường hoặc application.properties
    @Value("${jwt.secret}")
    private String secret;

    // Thời gian hiệu lực của JWT tính bằng mili giây (ví dụ: 24 giờ)
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // --- Các phương thức tạo JWT ---

    /**
     * Tạo JWT từ thông tin chi tiết người dùng.
     *
     * @param userDetails Đối tượng UserDetails của Spring Security.
     * @return Chuỗi JWT đã được tạo.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Bạn có thể thêm các claims tùy chỉnh vào đây nếu cần, ví dụ: vai trò (roles)
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Tạo JWT thực tế với các claims và chủ thể (subject - thường là username).
     *
     * @param claims Các thông tin tùy chỉnh (claims) muốn đưa vào JWT.
     * @param subject Chủ thể của token (thường là username).
     * @return Chuỗi JWT đã được ký.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact(); // Nén token thành chuỗi JWS compact URL-safe
    }

    /**
     * Lấy khóa ký bí mật từ chuỗi secret.
     *
     * @return Đối tượng Key dùng để ký/xác thực JWT.
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }



    /**
     * Trích xuất username từ JWT.
     *
     * @param token Chuỗi JWT.
     * @return Username được trích xuất.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Trích xuất thời gian hết hạn từ JWT.
     *
     * @param token Chuỗi JWT.
     * @return Thời gian hết hạn của token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Trích xuất một claim cụ thể từ JWT.
     *
     * @param token Chuỗi JWT.
     * @param claimsResolver Hàm để giải quyết claim mong muốn.
     * @param <T> Kiểu dữ liệu của claim.
     * @return Giá trị của claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Trích xuất tất cả các claims từ JWT.
     *
     * @param token Chuỗi JWT.
     * @return Đối tượng Claims chứa tất cả các thông tin trong payload của JWT.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



    /**
     * Kiểm tra xem JWT đã hết hạn chưa.
     *
     * @param token Chuỗi JWT.
     * @return True nếu token đã hết hạn, ngược lại là False.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Kiểm tra tính hợp lệ của JWT.
     *
     * @param token Chuỗi JWT.
     * @param userDetails Đối tượng UserDetails để so sánh username.
     * @return True nếu token hợp lệ (username khớp và chưa hết hạn), ngược lại là False.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
}