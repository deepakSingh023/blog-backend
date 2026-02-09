package com.example.blog_backend.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UpdateBlog(
                         String title,

                         String content,

                         List<String> tags,

                         String blogId
) {
}
