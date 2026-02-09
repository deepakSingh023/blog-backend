package com.example.blog_backend.controller;


import com.example.blog_backend.dto.AuthResponse;
import com.example.blog_backend.dto.AuthResponseToken;
import com.example.blog_backend.dto.LoginRequestDto;
import com.example.blog_backend.dto.RegisterDto;
import com.example.blog_backend.service.AuthResult;
import com.example.blog_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(
            @RequestBody RegisterDto data) {

        AuthResult res = authService.register(data);

        return ResponseEntity
                .status(201)
                .body(new AuthResponse(
                        res.token(),
                        res.email(),
                        res.userId(),
                        res.username(),
                        res.role().toString()
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(
            @RequestBody LoginRequestDto data) {

        AuthResult result = authService.login(data);

        return ResponseEntity.ok(
                new AuthResponse(
                        result.token(),
                        result.email(),
                        result.userId(),
                        result.username(),
                        result.role().toString()
                )
        );
    }

}

