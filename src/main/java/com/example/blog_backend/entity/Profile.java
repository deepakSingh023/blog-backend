package com.example.blog_backend.entity;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("profile")
@Builder
public class Profile {

    @Id
    private String id;

    private String userId;

    private String username;

    private String email;

    private String bio;

    private String profilePic;

    private String profilePicPublicId;

    private Instant createdAt;

    @Transient
    private Double score;
}
