package com.example.blog_backend.repository;

import com.example.blog_backend.entity.Comments;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comments, String> {

     boolean ExistsByUserIdAndBlogId(String userId, String blogId);
}
