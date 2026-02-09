package com.example.blog_backend.service;


import com.example.blog_backend.dto.*;
import com.example.blog_backend.entity.Blog;
import com.example.blog_backend.entity.Likes;
import com.example.blog_backend.entity.Profile;
import com.example.blog_backend.enums.FolderType;
import com.example.blog_backend.enums.ImageType;
import com.example.blog_backend.exceptions.BlogNotFound;
import com.example.blog_backend.exceptions.LikesNotFound;
import com.example.blog_backend.exceptions.UserDoesntExist;
import com.example.blog_backend.repository.AuthRepository;
import com.example.blog_backend.repository.BlogRepository;
import com.example.blog_backend.repository.LikesRepository;
import com.example.blog_backend.repository.ProfileRepository;
import com.example.blog_backend.util.BlogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.AccessDeniedException;

import java.awt.print.Pageable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
//create,update and get working
@Service
public class BlogServiceImpl implements BlogService{

    private final BlogRepository blogRepository;
    private final AuthRepository authRepository;
    private final ImageStorageService imageStorageService;
    private final LikesRepository likesRepository;
    private final ProfileRepository profileRepository;


    public BlogServiceImpl(
            BlogRepository blogRepository,
            AuthRepository authRepository,
            ImageStorageService imageStorageService, ProfileRepository profileRepository, LikesRepository likesRepository) {
        System.out.println("🔴 BlogServiceImpl CONSTRUCTOR called");
        this.blogRepository = blogRepository;
        this.authRepository = authRepository;
        this.imageStorageService = imageStorageService;
        this.profileRepository = profileRepository;
        this.likesRepository = likesRepository;

    }

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

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(()-> new UserDoesntExist(" user doesnt exist"));

        if (image != null && !image.isEmpty()) {
            if(!image.getContentType().startsWith("image/")){
                throw new IllegalArgumentException("only images allowed");
            }

        }

        CloudinaryUploadResult links = imageStorageService.uploadProfileImage(image, ImageType.BLOG, FolderType.BLOG);

        Blog create = Blog.builder()
                .userId(userId)
                .content(data.content())
                .image(links.profileUrl())
                .blogImagePublicId(links.profilePicPublicId())
                .username(profile.getUsername())
                .userImage(profile.getProfilePic())
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
    public Blog updateBlog(String userId, UpdateBlog data, MultipartFile image){
        UUID userUuid;

        try{
            userUuid = UUID.fromString(userId);
        }catch(IllegalArgumentException ex){
            throw new IllegalArgumentException("userid is wrong");
        }

        if(!authRepository.existsById(userUuid)){
            throw new UserDoesntExist("user doesnt exist");
        }

        Blog blog = blogRepository.findById(data.blogId())
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


        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() ->new BlogNotFound("blog not found"));

        if(!blog.getUserId().equals(userId)){
            throw new AccessDeniedException("you cannot access this blog");

        }

        if(blog.getBlogImagePublicId()!=null){
            imageStorageService.deleteImage(blog.getBlogImagePublicId());
        }

        blogRepository.delete(blog);

    }

    @Override
    public PagedResponse<BlogResponse> getBlogs(String userId, Instant cursor , int limit){





        List<Blog> blogs;

        if(cursor == null){
             blogs = blogRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
        }else{
             blogs= blogRepository.findTop10ByUserIdAndCreatedAtLessThanOrderByCreatedAtDesc(userId,cursor);

        }

        if(blogs.isEmpty()){
            throw new BlogNotFound("no blogs found");
        }

        Instant nextCursor = blogs.isEmpty()? null : blogs.get(blogs.size()-1).getCreatedAt();

        List<BlogResponse> allBlog = blogs.stream()
                .map(BlogMapper::toResponse)
                .toList();


        return new PagedResponse<>(allBlog , nextCursor);
    }

    @Override
    public SingleBlogResponse getSingleBlog(String blogId,String userId){

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(()-> new BlogNotFound("blog not found"));

        Likes like = likesRepository.findByUserId(userId);

        SingleBlogResponse res = new SingleBlogResponse(
                blog.getId(),
                blog.getTitle(),
                blog.getContent(),
                blog.getTags(),
                blog.getUserId().equals(userId),
                blog.getImage(),
                blog.getLikes(),
                blog.getComments(),
                like!=null && like.getUserId().equals(userId),
                blog.getUsername(),
                blog.getUserImage(),
                blog.getCreatedAt()
        );

        return res;


    }


}
