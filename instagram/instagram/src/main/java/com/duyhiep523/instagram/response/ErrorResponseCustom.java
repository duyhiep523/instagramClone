package com.duyhiep523.instagram.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseCustom {
    int code;
    String message;
    LocalDateTime timestamp;
    Map<String, String> errors;
}