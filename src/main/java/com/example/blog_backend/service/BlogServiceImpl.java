package com.example.blog_backend.service;


import com.example.blog_backend.dto.CloudinaryUploadResult;
import com.example.blog_backend.dto.CreateBlogDto;
import com.example.blog_backend.entity.Blog;
import com.example.blog_backend.enums.FolderType;
import com.example.blog_backend.enums.ImageType;
import com.example.blog_backend.exceptions.UserDoesntExist;
import com.example.blog_backend.repository.AuthRepository;
import com.example.blog_backend.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService{

    private final BlogRepository blogRepository;

    private final AuthRepository authRepository;
    private final ImageStorageService imageStorageService;

    @Override
    public Blog createBlog(String userId, CreateBlogDto data , MultipartFile image){

        UUID userUuid;
        try {
            userUuid = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid user ID format");
        }

        if(!authRepository.existsById(userUuid)){
            throw new UserDoesntExist("user doesnt exist");
        }

        if (image != null && !image.isEmpty()) {
            if(!image.getContentType().startsWith("/image")){
                throw new IllegalArgumentException("only images allowed");
            }

        }

        CloudinaryUploadResult links = imageStorageService.uploadProfileImage(image, ImageType.BLOG, FolderType.BLOG);

        Blog create = Blog.builder()
                .userId(userId)
                .content(data.content())
                .image(links.profileUrl())
                .blogImagePublicId(links.profilePicPublicId())
                .tags(data.tags())
                .title(data.title())
                .createdAt(Instant.now())
                .comments(0L)
                .likes(0L)
                .build();

        blogRepository.save(create);
        return create;

    }
}
