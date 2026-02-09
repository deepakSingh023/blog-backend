package com.example.blog_backend.dto;

import com.example.blog_backend.entity.Blog;

import java.time.Instant;
import java.util.List;

public record FeedResponse(
        List<FeedBlogDto> blogs,
        Instant cursor
) {
}
