package com.example.blog_backend.service;

import com.example.blog_backend.dto.LoginRequestDto;
import com.example.blog_backend.dto.RegisterDto;
import com.example.blog_backend.entity.User;
import com.example.blog_backend.exceptions.UserAlreadyExistsException;
import com.example.blog_backend.exceptions.UserDoesntExist;
import com.example.blog_backend.exceptions.WrongPassword;
import com.example.blog_backend.repository.AuthRepository;
import com.example.blog_backend.enums.UserRole;
import com.example.blog_backend.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

//register login both working
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ProfileService profileService;


    @Transactional
    @Override
    public AuthResult register(RegisterDto data){

        if(authRepository.existsByEmail(data.getEmail()) || authRepository.existsByUsername(data.getUsername())){
             throw new UserAlreadyExistsException("User already exists");
        }
        String encryptedPassword = passwordEncoder.encode(data.getPassword());



        User user = User.builder()
                .username(data.getUsername())
                .email(data.getEmail())
                .password(encryptedPassword)
                .createdAt(Instant.now())
                .role(UserRole.USER)
                .build();

        authRepository.save(user);


        List<String> roles = List.of(user.getRole().name());
        String userId = user.getId().toString();

        String token= jwtUtil.generateToken(userId, roles);


        profileService.createProfile( user.getId().toString(), data );

        return new AuthResult(
                token,
                user.getEmail(),
                userId,
                user.getUsername(),
                UserRole.USER
        );

    }

    @Override
    public AuthResult login(LoginRequestDto data) {

        User user = authRepository.findByEmail(data.getEmail())
                .orElseThrow(() -> new UserDoesntExist("User not found"));

        if (!passwordEncoder.matches(data.getPassword(), user.getPassword())) {
            throw new WrongPassword("Wrong password");
        }

        List<String> roles = List.of(user.getRole().name());
        String token = jwtUtil.generateToken(user.getId().toString(), roles);
        String userId = user.getId().toString();

        return new AuthResult(
                token,
                user.getEmail(),
                userId,
                user.getUsername(),
                UserRole.USER
        );
    }



}
