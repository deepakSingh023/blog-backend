package com.example.blog_backend.repository;

import com.example.blog_backend.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface BlogRepository extends MongoRepository<Blog,String> {

    public Optional<Blog> findByUserId( UUID userId);


    // First page: no cursor
    List<Blog> findTop10ByUserIdOrderByCreatedAtDesc(UUID userId);

    // Next pages: cursor exists
    List<Blog> findTop10ByUserIdAndCreatedAtLessThanOrderByCreatedAtDesc(UUID userId, Instant cursor);

}
