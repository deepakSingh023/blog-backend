package com.example.blog_backend.dto;


import jakarta.validation.constraints.NotBlank;

public record UpdateProfile(

        @NotBlank
        String bio

) {
}
