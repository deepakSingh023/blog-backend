package com.example.blog_backend.controller;


import com.example.blog_backend.service.LikesCommentService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
@RequestMapping("api/like&comment")
public class LikesAndCommentController {

    private final LikesCommentService likesCommentService;

    @PostMapping("/like") // Add the mapping
    public ResponseEntity<String> createLike(
            @RequestParam String blogId,
            Authentication auth
    ) {
        String userId = auth.getName();

        boolean success = likesCommentService.createLikes(userId, blogId);
        if (success) {
            return ResponseEntity.ok("Like created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create like");
        }
    }


    @PostMapping("/removeLike")
     public ResponseEntity<Void> removeLike(
             @RequestParam String blogId,
             Authentication auth
    ){

        String userId = auth.getName();

        likesCommentService.removeLikes(userId,blogId);

        return ResponseEntity.ok().build();


    }
}
