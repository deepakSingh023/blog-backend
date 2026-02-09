package com.example.blog_backend.entity;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Document("blog")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Blog {

    @Id
    private String id;

    private String userId;

    private String username;

    private String userImage;

    private String image;

    private String blogImagePublicId;

    private String title;

    private String content;

    private List<String> tags;

    private Instant createdAt;

    private Long likes;

    private Long comments;

    @TextScore
    private Double score;
}
