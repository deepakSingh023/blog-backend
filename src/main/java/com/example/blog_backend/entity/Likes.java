package com.example.blog_backend.entity;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@AllArgsConstructor
@Builder
@Data
public class Likes {

    @Id
    private String id;

    private String blogId;

    private String userId;

    private Instant createdAt;

}
