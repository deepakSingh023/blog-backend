package com.example.blog_backend.dto;

import java.time.Instant;

public record FeedBlogDto(
        String id,
        String title,
        String content,
        long likeCount,
        boolean likedByMe,
        Instant createdAt
) {}
