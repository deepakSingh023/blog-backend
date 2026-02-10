package com.example.blog_backend.controller;


import com.example.blog_backend.dto.*;
import com.example.blog_backend.entity.Blog;
import com.example.blog_backend.service.BlogService;
import com.example.blog_backend.util.BlogMapper;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/blog")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService){
        this.blogService = blogService;
    }


    @PostMapping(value="/create" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BlogResponse> create(

            @RequestPart(value = "image" , required = true) MultipartFile image,
            @RequestPart(value = "createBlog" , required=true ) CreateBlogDto data,
            Authentication auth
            ){

        System.out.print("reached blog controller");

        String userId = auth.getName();

        Blog response = blogService.createBlog(userId,data, image);

        BlogResponse filtered = new BlogResponse(
                response.getTitle(),
                response.getId(),
                response.getContent(),
                response.getTags(),
                response.getImage(),
                response.getLikes(),
                response.getComments(),
                response.getCreatedAt()
        );

        return ResponseEntity.ok(filtered);

        // never pass the entity directly to the response map it to a dto then send entity have sensitive data



    }

    @PutMapping(value="/update",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BlogResponse> update(
            @RequestPart(value = "image" , required = false) MultipartFile image,
            @RequestPart(value = "updateBlog") UpdateBlog data,
            Authentication auth

    ){

        String userId = auth.getName();

        System.out.println(data.blogId());

        Blog response = blogService.updateBlog(userId, data, image);

        BlogResponse filtered = new BlogResponse(
                response.getId(),
                response.getTitle(),
                response.getContent(),
                response.getTags(),
                response.getImage(),
                response.getLikes(),
                response.getComments(),
                response.getCreatedAt()
        );

        return ResponseEntity.ok(filtered);




    }

    @GetMapping("getBlog")
    public ResponseEntity<SingleBlogResponse> getBlog(
            @RequestParam(required = false) String userId,
            @RequestParam(required = true) String blogId
    ){

        SingleBlogResponse res = blogService.getSingleBlog(blogId,userId);

        return ResponseEntity.ok(res);
    }


    @DeleteMapping("/delete/{blogId}")
    public ResponseEntity<?> delete(
            Authentication auth,
            @RequestParam String blogId
    ){

        String userId = auth.getName();
        blogService.deleteBlog(blogId,userId);


        return ResponseEntity.noContent().build();

    }

    @GetMapping("/getall")
    public ResponseEntity<PagedResponse<BlogResponse>> getAll(
            @RequestParam(required = false) Instant cursor,
            @RequestParam(defaultValue = "10",required = false ) int limit,
            Authentication auth
    ){

        String userId = auth.getName();

        PagedResponse<BlogResponse> blogs= blogService.getBlogs(userId, cursor, limit);



        return ResponseEntity.ok(blogs);

    }
}
