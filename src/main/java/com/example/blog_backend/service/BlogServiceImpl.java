package com.example.blog_backend.service;


import com.example.blog_backend.dto.CloudinaryUploadResult;
import com.example.blog_backend.dto.CreateBlogDto;
import com.example.blog_backend.entity.Blog;
import com.example.blog_backend.enums.FolderType;
import com.example.blog_backend.enums.ImageType;
import com.example.blog_backend.exceptions.BlogNotFound;
import com.example.blog_backend.exceptions.UserDoesntExist;
import com.example.blog_backend.repository.AuthRepository;
import com.example.blog_backend.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.AccessDeniedException;

import java.time.Instant;
import java.util.List;
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
            if(!image.getContentType().startsWith("image/")){
                throw new IllegalArgumentException("only images allowed");
            }

        }

        CloudinaryUploadResult links = imageStorageService.uploadProfileImage(image, ImageType.BLOG, FolderType.BLOG);

        Blog create = Blog.builder()
                .userId(userUuid)
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

    @Override
    public Blog updateBlog(String userId, CreateBlogDto data, MultipartFile image){
        UUID userUuid;

        try{
            userUuid = UUID.fromString(userId);
        }catch(IllegalArgumentException ex){
            throw new IllegalArgumentException("userid is wrong");
        }

        if(!authRepository.existsById(userUuid)){
            throw new UserDoesntExist("user doesnt exist");
        }

        Blog blog = blogRepository.findByUserId(userUuid)
                .orElseThrow( () -> new UserDoesntExist("blog doesnt exist"));

        if(data.content()!=null){
            blog.setContent(data.content());
        }

        if(data.tags()!= null){
            blog.setTags(data.tags());
        }

        if(data.title()!=null){
            blog.setTitle(data.title());
        }

        if(image != null && !image.isEmpty() ){
            if(!image.getContentType().startsWith("image/")){
                throw new IllegalArgumentException("please provide an image");
            }
            if(blog.getBlogImagePublicId() !=null){
                imageStorageService.deleteImage(blog.getBlogImagePublicId());
            }

            CloudinaryUploadResult url = imageStorageService.uploadProfileImage(image,ImageType.BLOG,FolderType.BLOG);

            blog.setImage(url.profileUrl());
            blog.setBlogImagePublicId(url.profilePicPublicId());



        }

        blogRepository.save(blog);

        return blog;
    }

    @Override
    public void deleteBlog(String blogId, String userId){

        UUID userUuid ;

        try{
            userUuid = UUID.fromString(userId);
        }catch(IllegalArgumentException ex){
            throw new IllegalArgumentException("please provide a string userid");
        }

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() ->new BlogNotFound("blog not found"));

        if(!blog.getUserId().equals(userUuid)){
            throw new AccessDeniedException("you cannot access this blog");

        }

        if(blog.getBlogImagePublicId()!=null){
            imageStorageService.deleteImage(blog.getBlogImagePublicId());
        }

        blogRepository.delete(blog);

    }

    @Override
    public List<Blog> getBlogs(String userId){

        UUID userUuid;

        try{
            userUuid = UUID.fromString(userId);
        }catch(IllegalArgumentException ex){
            throw new IllegalArgumentException("provide a valid userId");
        }

        List<Blog> blogs = blogRepository.findAllByUserId(userUuid);

        if(blogs.isEmpty()){
            throw new BlogNotFound("no blogs found");
        }


        return blogs;
    }
}
