package com.example.blog_backend.service;

import com.example.blog_backend.dto.CreateBlogDto;
import com.example.blog_backend.entity.Blog;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BlogService {

    Blog createBlog(String userId , CreateBlogDto data , MultipartFile image);

    Blog updateBlog(String userId, CreateBlogDto data, MultipartFile image);

    void deleteBlog(String blogId , String userId);

    List<Blog> getBlogs(String userId);
}
