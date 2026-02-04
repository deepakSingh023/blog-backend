package com.example.blog_backend.repository;

import com.example.blog_backend.entity.Likes;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LikesRepository extends MongoRepository<Likes, String> {
    boolean existsByUserIdAndBlogId(String userId, String blogId);

    Optional<Likes> findByUserIdAndBlogId(String userId, String blogId);
}
