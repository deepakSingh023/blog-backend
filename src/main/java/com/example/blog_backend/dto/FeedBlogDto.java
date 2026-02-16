package com.example.blog_backend.dto;

import java.time.Instant;
import java.util.List;

public record FeedBlogDto(
        String id,
        String userId,
        String title,
        String content,
        String image,
        long likeCount,
        Long Comments,
        String username,
        String userImage,
        boolean likedByMe,
        Instant createdAt
) {}
