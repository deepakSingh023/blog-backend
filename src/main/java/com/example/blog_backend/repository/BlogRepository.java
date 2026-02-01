package com.example.blog_backend.repository;

import com.example.blog_backend.entity.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface BlogRepository extends MongoRepository<Blog,String> {

    public Optional<Blog> findByUserId( UUID userId);


    public List<Blog> findAllByUserId(UUID userId);
}
