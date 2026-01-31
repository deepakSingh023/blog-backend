package com.example.blog_backend.repository;

import com.example.blog_backend.entity.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BlogRepository extends MongoRepository<Blog,String> {
}
