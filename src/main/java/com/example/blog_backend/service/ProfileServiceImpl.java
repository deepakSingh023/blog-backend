package com.example.blog_backend.service;


import com.example.blog_backend.dto.CloudinaryUploadResult;
import com.example.blog_backend.dto.RegisterDto;
import com.example.blog_backend.dto.UpdateProfile;
import com.example.blog_backend.entity.Profile;
import com.example.blog_backend.exceptions.UserAlreadyExistsException;
import com.example.blog_backend.exceptions.UserDoesntExist;
import com.example.blog_backend.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{

    private final ProfileRepository profileRepository;
    private final ImageStorageService imageStorageService;

    @Override
    public Profile createProfile(String userId, RegisterDto data){

        if(profileRepository.existsByUserId(userId)){
            throw new UserAlreadyExistsException("profile already exists");
        }

        Profile profile = Profile.builder()
                .userId(userId)
                .email(data.getEmail())
                .username(data.getUsername())
                .createdAt(Instant.now())
                .build();

        profileRepository.save(profile);

        return profile;

    }

    @Override
    public Profile updateProfile(String userId , String bio , MultipartFile image){

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(()-> new UserDoesntExist("profile doesnt exist"));

        if (bio != null) {
            profile.setBio(bio);
        }

        if (image != null && !image.isEmpty()) {

            if (!image.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("Only image files allowed");
            }
            if(profile.getProfilePicPublicId()!=null){
                imageStorageService.deleteImage(profile.getProfilePicPublicId());
            }
            CloudinaryUploadResult url = imageStorageService.uploadProfileImage(image);

            profile.setProfilePic(url.profileUrl());
            profile.setProfilePicPublicId(url.profilePicPublicId());
        }

        return profileRepository.save(profile);


    }
}
