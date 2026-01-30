package com.example.blog_backend.service;


import com.example.blog_backend.dto.RegisterDto;
import com.example.blog_backend.dto.UpdateProfile;
import com.example.blog_backend.entity.Profile;
import com.example.blog_backend.exceptions.UserAlreadyExistsException;
import com.example.blog_backend.exceptions.UserDoesntExist;
import com.example.blog_backend.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{

    private final ProfileRepository profileRepository;

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
    public Profile updateProfile(String userId , UpdateProfile data){
        if(!profileRepository.existsByUserId(userId)){
            throw new UserDoesntExist("profile doesnt exists");
        }

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(()-> new RuntimeException("profile doesnt exist"));

        if (data.bio() != null) {
            profile.setBio(data.bio());
        }

        if (data.profilePic() != null) {
            profile.setProfilePic(data.profilePic());
        }

        return profileRepository.save(profile);


    }
}
