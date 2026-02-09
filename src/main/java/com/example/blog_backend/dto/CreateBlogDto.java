package com.example.blog_backend.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateBlogDto(

        @NotBlank
        String title,

        @NotBlank
        String content,


        List<String> tags

) {
}
