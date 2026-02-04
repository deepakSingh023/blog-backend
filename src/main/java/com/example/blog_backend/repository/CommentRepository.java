package com.example.blog_backend.repository;

import com.example.blog_backend.entity.Comments;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comments, String> {

     boolean existsByUserIdAndBlogId(String userId, String blogId);

     List<Comments> findAllByUserId(String userId);
}
