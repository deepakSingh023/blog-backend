package com.example.blog_backend.service;

import com.example.blog_backend.dto.RegisterDto;
import com.example.blog_backend.dto.ReturnProfile;
import com.example.blog_backend.dto.UpdateProfile;
import com.example.blog_backend.entity.Profile;
import org.springframework.web.multipart.MultipartFile;
//both working perfectly
public interface ProfileService {

    Profile createProfile(String userId, RegisterDto data);
    Profile updateProfile(String userId,String bio,  MultipartFile image );

    ReturnProfile getProfile(String userId);
}
