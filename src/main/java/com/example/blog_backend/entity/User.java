package com.example.blog_backend.entity;


import com.example.blog_backend.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;


import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name =  "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
}
