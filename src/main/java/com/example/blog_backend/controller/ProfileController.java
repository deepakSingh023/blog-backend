package com.example.blog_backend.controller;


import com.example.blog_backend.dto.ReturnProfile;
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


    @PostMapping(value = "/update", consumes = "multipart/form-data")
    public ResponseEntity<ReturnProfile> updateProfile(
            @RequestParam(value = "bio", required = false) String bio,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Authentication auth
    ){

        String userId = auth.getName();
        System.out.println(">>> SUCCESS: Reached Controller!");
        System.out.println(">>> User from Auth: " + userId);

        System.out.println(bio);


        Profile updatedProfile = profileService.updateProfile(userId, bio, image);

        ReturnProfile response = new ReturnProfile(
                updatedProfile.getUsername(),
                updatedProfile.getBio(),
                updatedProfile.getProfilePic()
        );



        return ResponseEntity.ok(response);
    }

    @GetMapping("/getProfile")
    public ResponseEntity<ReturnProfile> getProfile(
            Authentication auth
    ){

        String userId = auth.getName();

        ReturnProfile res = profileService.getProfile(userId);

        return ResponseEntity.ok(res);

    }

}
