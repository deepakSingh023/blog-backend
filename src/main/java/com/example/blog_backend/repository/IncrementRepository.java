package com.example.blog_backend.repository;


import com.example.blog_backend.entity.Blog;
import com.example.blog_backend.entity.Likes;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IncrementRepository {

    private final MongoTemplate mongoTemplate;


    @Async("decIncExecutor")
    public boolean commentIncrement(String blogId){
        UpdateResult result = mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(blogId)),
                new Update().inc("comment",1),
                Blog.class
        );

        return result.getMatchedCount()==1;
    }

    @Async("decIncExecutor")
    public boolean likesIncrement(String blogId){
        UpdateResult result = mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(blogId)),
                new Update().inc("likes",1),
                Blog.class
        );

        return result.getMatchedCount()==1;
    }

    @Async("decIncExecutor")
    public boolean likesDecrement(String blogId){
        UpdateResult result = mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(blogId)),
                new Update().inc("likes",-1),
                Blog.class
        );

        return result.getMatchedCount()==1;
    }

    @Async("decIncExecutor")
    public boolean commentDecrement(String blogId){
        UpdateResult result = mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(blogId)),
                new Update().inc("comment",-1),
                Blog.class
        );

        return result.getMatchedCount()==1;
    }


    }



