package com.example.blog_backend.dto;

public record AuthResponse(
        String token,
        String email,
        String username,
        String role
) {}
