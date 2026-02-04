package com.example.blog_backend.repository;

import com.example.blog_backend.entity.Blog;
import com.example.blog_backend.entity.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface BlogRepository extends MongoRepository<Blog,String> {

    Optional<Blog> findByUserId( String userId);

    List<Blog> findAllByUserId(String userId);


    // First page: no cursor
    List<Blog> findTop10ByUserIdOrderByCreatedAtDesc(String userId);

    List<Blog> findTop10ByOrderByCreatedAtDesc();

    List<Blog> findTop10ByCreatedAtBeforeOrderByCreatedAtDesc(
            Instant createdAt
    );


    // Next pages: cursor exists
    List<Blog> findTop10ByUserIdAndCreatedAtLessThanOrderByCreatedAtDesc(String userId, Instant cursor);

}
