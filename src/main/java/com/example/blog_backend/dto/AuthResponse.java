package com.example.blog_backend.dto;

import com.example.blog_backend.enums.UserRole;

public record AuthResponse(
        String token,
        String email,
        String id,
        String username,
        String role
) {}
