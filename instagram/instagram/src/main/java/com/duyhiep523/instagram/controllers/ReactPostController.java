package com.duyhiep523.instagram.controllers;

import com.duyhiep523.instagram.response.Response;
import com.duyhiep523.instagram.services.Iservices.IReactionPostService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${apiPrefix}/reactions")
@RequiredArgsConstructor
public class ReactPostController {
    private final IReactionPostService reactionService;

    @PostMapping("/{userId}/{postId}")
    public ResponseEntity<?> addReactionToPost(@PathVariable String userId, @PathVariable String postId) {
        reactionService.addReactionToPost(userId, postId);
        Response<Object> response = Response.builder()
                .code(HttpStatus.CREATED.value())
                .message("Thả cảm xúc thành công")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

//    @DeleteMapping("/{userId}/{postId}")
//    public ResponseEntity<?> removeReactionFromPost(@PathVariable String userId, @PathVariable String postId) {
//        reactionService.removeReactionFromPost(userId, postId);
//        Response<Object> response = Response.builder()
//                .code(HttpStatus.OK.value())
//                .message("Gỡ cảm xúc thành công")
//                .build();
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/count/{postId}")
    public ResponseEntity<?> getReactionCount(@PathVariable String postId) {
        int count = reactionService.getReactionCountByPost(postId);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Lấy số lượng cảm xúc thành công")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{userId}/{postId}")
    public ResponseEntity<?> removeReaction(@PathVariable String userId, @PathVariable String postId) {
        reactionService.removeReactionFromPost(userId, postId);
        Response<Object> response = Response.builder()
                .code(HttpStatus.OK.value())
                .message("Đã xoá cảm xúc thành công")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}
