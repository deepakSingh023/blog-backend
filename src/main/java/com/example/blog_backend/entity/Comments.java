package com.example.blog_backend.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@Data
@AllArgsConstructor
@Builder
@Document("comments")
public class Comments {

    @Id
    private String id;

    private String userId;

    private String blogId;

    private String username;

    private String userImage;

    private String content;

    private Instant createdAt;

    private boolean IsDeleted;
}
