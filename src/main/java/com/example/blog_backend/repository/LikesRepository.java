package com.example.blog_backend.repository;

import com.example.blog_backend.entity.Likes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends MongoRepository<Likes, String> {
    boolean existsByUserIdAndBlogId(String userId, String blogId);

    Optional<Likes> findByUserIdAndBlogId(String userId, String blogId);

    @Query("""
        SELECT bl.blogId
        FROM BlogLike bl
        WHERE bl.userId = :userId
          AND bl.blogId IN :blogIds
    """)
    List<String> findLikedBlogIdsByUser(
            @Param("userId") String userId,
            @Param("blogIds") List<String> blogIds
    );
}
