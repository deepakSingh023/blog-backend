package com.example.blog_backend.service;

import com.example.blog_backend.enums.UserRole;

public record AuthResult(
        String token,
        String email,
        String userId,
        String username,
        UserRole role
) {}
