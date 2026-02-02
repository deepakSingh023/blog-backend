package com.example.blog_backend.dto;

public record CommentCreation(
        String blogId,
        String content
) {
}
