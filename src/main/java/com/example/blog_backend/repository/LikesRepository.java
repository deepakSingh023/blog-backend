package com.example.blog_backend.repository;

import com.example.blog_backend.entity.Likes;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LikesRepository extends MongoRepository<Likes, String> {
    boolean existsByUserIdAndBlogId(String userId, String blogId);
}
