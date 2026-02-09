package com.example.blog_backend.dto;

import java.time.Instant;

public record CommentResponse(
        String id,
        String userId,
        String blogId,
        String content,
        Instant createdAt,
        String username,
        String profilePic,
        boolean  IsDeleted,
        boolean IsDeletable
) {
}
