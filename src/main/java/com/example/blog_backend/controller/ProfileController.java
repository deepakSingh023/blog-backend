package com.example.blog_backend.controller;


import com.example.blog_backend.dto.UpdateProfile;
import com.example.blog_backend.entity.Profile;
import com.example.blog_backend.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.Authenticator;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/profile")
    public RequestEntity<Profile> updateProfile(
            @RequestPart(value = "bio", required = false) String bio,
            @RequestPart(value = "image", required = false) MultipartFile image,
            Authentication auth
    ){
        String userId = auth.getName();

        Profile updatedProfile = profileService.updateProfile(userId, bio, image);

        ResponseEntity.ok(updatedProfile);

    }

}
