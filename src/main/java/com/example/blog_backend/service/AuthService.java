package com.example.blog_backend.service;

import com.example.blog_backend.dto.LoginRequestDto;
import com.example.blog_backend.dto.RegisterDto;
import com.example.blog_backend.entity.User;

public interface AuthService {

    AuthResult register(RegisterDto data);

    AuthResult login(LoginRequestDto data);
}
