package com.example.blog_backend.dto;

import java.time.Instant;
import java.util.List;

public record SingleBlogResponse(


        String id,

        String title,


        String content,
        List<String> tags,

        boolean editable,

        String image,

        Long likes,

        Long comments,

        boolean liked,

        String username,

        String userImage,


        Instant createdAt
) {
}
