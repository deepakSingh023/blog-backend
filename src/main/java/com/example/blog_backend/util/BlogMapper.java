package com.example.blog_backend.util;

import com.example.blog_backend.dto.BlogResponse;
import com.example.blog_backend.entity.Blog;

public class BlogMapper {

    public static BlogResponse toResponse(Blog blog) {
        return new BlogResponse(
                blog.getId(),
                blog.getTitle(),
                blog.getContent(),
                blog.getTags(),
                blog.getImage(),
                blog.getLikes(),
                blog.getComments(),
                blog.getCreatedAt()
        );


    }
}
