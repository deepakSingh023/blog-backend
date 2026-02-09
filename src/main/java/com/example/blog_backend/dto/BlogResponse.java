package com.example.blog_backend.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.List;

public record BlogResponse(


        String id,

        String title,


        String content,
        List<String> tags,




        String image,

        Long likes,

        Long comments,


        Instant createdAt
) {
}
