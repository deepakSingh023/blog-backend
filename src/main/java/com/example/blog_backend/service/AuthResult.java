package com.example.blog_backend.service;

public record AuthResult(
        String token,
        String email,
        String username,
        String role
) {}
