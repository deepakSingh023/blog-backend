package com.example.blog_backend.service;


import com.example.blog_backend.dto.CloudinaryUploadResult;
import com.example.blog_backend.dto.RegisterDto;
import com.example.blog_backend.dto.ReturnProfile;
import com.example.blog_backend.dto.UpdateProfile;
import com.example.blog_backend.entity.Blog;
import com.example.blog_backend.entity.Comments;
import com.example.blog_backend.entity.Profile;
import com.example.blog_backend.enums.FolderType;
import com.example.blog_backend.enums.ImageType;
import com.example.blog_backend.exceptions.UserAlreadyExistsException;
import com.example.blog_backend.exceptions.UserDoesntExist;
import com.example.blog_backend.repository.AuthRepository;
import com.example.blog_backend.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ImageStorageService imageStorageService;
    private final BlogAndCommentDenormalization blogAndCommentDenormalization;

    private final MongoTemplate mongoTemplate;
    private final AuthRepository authRepository;

    @Override
    public Profile createProfile(String userId, RegisterDto data) {

        if (profileRepository.existsByUserId(userId)) {
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


    //@Transactional
    @Override
    public Profile updateProfile(String userId, String bio, MultipartFile image) {

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserDoesntExist("profile doesnt exist"));

        System.out.print(profile);

        if (bio != null) {
            profile.setBio(bio);
        }

        String profileNew= null;

        if (image != null && !image.isEmpty()) {

            if (!image.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("Only image files allowed");
            }
            if (profile.getProfilePicPublicId() != null) {
                imageStorageService.deleteImage(profile.getProfilePicPublicId());
            }
            CloudinaryUploadResult url = imageStorageService.uploadProfileImage(image, ImageType.PROFILE, FolderType.PROFILE);

            profile.setProfilePic(url.profileUrl());
            profile.setProfilePicPublicId(url.profilePicPublicId());

            profileNew = url.profileUrl();

        }



        Profile saved = profileRepository.save(profile);

        if(profileNew !=null){
            blogAndCommentDenormalization.denormalizeCommentAndBlog(userId, profileNew);
        }
        return  saved;
    }

    @Override
    public ReturnProfile getProfile(String userId){

        UUID userUuid;

        try{
            userUuid = UUID.fromString(userId);

        }catch(IllegalArgumentException ex){
            throw new RuntimeException("Illegal id format");
        }

        if(!authRepository.existsById(userUuid)){
            throw new UserDoesntExist(" user doesnt exist");
        }

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(()-> new UserDoesntExist("no profile found for user"));

        return new ReturnProfile(
                profile.getUsername(),
                profile.getBio(),
                profile.getProfilePic()
        );
    }

}
