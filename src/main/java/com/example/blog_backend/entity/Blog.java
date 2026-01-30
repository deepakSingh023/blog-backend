package com.example.blog_backend.entity;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document("blog")
@AllArgsConstructor
@Data
@Builder
public class Blog {

    @Id
    private String id;

    private String title;

    private String content;

    private List<String> tags;

    private Instant createdAt;

    private Long likes;

    private Long comments;
}
