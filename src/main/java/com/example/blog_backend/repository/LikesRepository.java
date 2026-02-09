package com.example.blog_backend.repository;

import com.example.blog_backend.dto.LikedBlogIdProjection;
import com.example.blog_backend.entity.Likes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends MongoRepository<Likes, String> {
    boolean existsByUserIdAndBlogId(String userId, String blogId);

    Optional<Likes> findByUserIdAndBlogId(String userId, String blogId);

    @Query(
            value = "{ 'userId': ?0, 'blogId': { $in: ?1 } }",
            fields = "{ 'blogId': 1, '_id': 0 }"
    )
    List<LikedBlogIdProjection> findLikedBlogIdsByUser(
            String userId,
            List<String> blogIds
    );


    Likes findByUserId(String userId);
}
