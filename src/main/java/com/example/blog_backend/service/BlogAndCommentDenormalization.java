package com.example.blog_backend.service;
import com.example.blog_backend.entity.Blog;
import com.example.blog_backend.entity.Comments;
import com.mongodb.MongoException;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BlogAndCommentDenormalization {

    private static final Logger log = LoggerFactory.getLogger(BlogAndCommentDenormalization.class);

    private final MongoTemplate mongoTemplate;


    @Async("blogExecutor")
    @Retryable(
            retryFor = {MongoException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void denormalizeCommentAndBlog(String userId, String profilePic){

        long start = System.currentTimeMillis();

        Query blogQuery = new Query(Criteria.where("userId").is(userId));
        Update update = new Update().set("userImage", profilePic);

        mongoTemplate.updateMulti(blogQuery, update, Blog.class);

        Query commentQuery = new Query(Criteria.where("userId").is(userId));
        mongoTemplate.updateMulti(commentQuery, update, Comments.class);

        long end = System.currentTimeMillis();

        log.info("Denormalization done for user {} in ms {}",
                userId, (end - start));
    }


    @Recover
    public void recover(Exception ex, String userId, String profilePic) {
        log.error("Denormalization permanently failed for user {}", userId, ex);
    }



}
