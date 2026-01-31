package com.example.blog_backend.controller;


import com.example.blog_backend.dto.BlogResponse;
import com.example.blog_backend.dto.CreateBlogDto;
import com.example.blog_backend.entity.Blog;
import com.example.blog_backend.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @PostMapping(value="/create" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BlogResponse> create(

            @RequestPart(value = "image" , required = true) MultipartFile image,
            @RequestPart(value = "createBlog" , required=true ) CreateBlogDto data,
            Authentication auth
            ){

        String userId = auth.getName();

        Blog response = blogService.createBlog(userId,data, image);

        BlogResponse filtered = new BlogResponse(
                response.getTitle(),
                response.getContent(),
                response.getTags(),
                response.getImage(),
                response.getCreatedAt()
        );

        return ResponseEntity.ok(filtered);

        // never pass the entity directly to the response map it to a dto then send entity have sensitive data



    }

    @PutMapping(value="/update",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BlogResponse> update(
            @RequestPart(value = "image" , required = false) MultipartFile image,
            @RequestPart(value = "updateBlog") CreateBlogDto data,
            Authentication auth

    ){

        String userId = auth.getName();

        Blog response = blogService.updateBlog(userId, data, image);

        BlogResponse filtered = new BlogResponse(
                response.getTitle(),
                response.getContent(),
                response.getTags(),
                response.getImage(),
                response.getCreatedAt()
        );

        return ResponseEntity.ok(filtered);




    }
}
