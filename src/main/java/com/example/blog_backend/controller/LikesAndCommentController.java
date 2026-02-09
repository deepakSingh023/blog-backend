package com.example.blog_backend.controller;


import com.example.blog_backend.dto.BlogResponse;
import com.example.blog_backend.dto.CommentCreation;
import com.example.blog_backend.dto.CommentResponse;
import com.example.blog_backend.service.LikesCommentService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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


    @PostMapping("create")
    public ResponseEntity<CommentResponse> create(
            Authentication auth,
            @RequestBody CommentCreation data
            ){

        String userId = auth.getName();

        CommentResponse res = likesCommentService.createComment(userId, data);

        return ResponseEntity.ok(res);

    }

    @DeleteMapping("removeComment")
    public ResponseEntity<String> removeComment(
            @RequestParam String commentId,
            Authentication auth
    ){

        String userId = auth.getName();


        likesCommentService.removeComment(commentId, userId);

        return ResponseEntity.ok("comment removed");

    }

    @GetMapping("get-comments")
    public ResponseEntity<List<CommentResponse>> getComments(
            @RequestParam(required = true) String blogId,
            @RequestParam(required = false) String userId

    ){

        List<CommentResponse> res  = likesCommentService.getComments(blogId, userId);

        return  ResponseEntity.ok(res);
    }

}
