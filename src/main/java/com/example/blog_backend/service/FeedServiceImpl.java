package com.example.blog_backend.service;


import com.example.blog_backend.dto.FeedBlogDto;
import com.example.blog_backend.dto.FeedResponse;
import com.example.blog_backend.dto.SearchResult;
import com.example.blog_backend.entity.Blog;
import com.example.blog_backend.entity.Profile;
import com.example.blog_backend.repository.BlogRepository;
import com.example.blog_backend.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;


import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

//both working
@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final MongoTemplate mongoTemplate;

    private final BlogRepository blogRepository;

    private final LikesRepository likesRepository;


    @Override
    public FeedResponse getFeed(Instant cursorCreatedAt, String userId) {

        List<Blog> blogs;

        if (cursorCreatedAt == null) {
            blogs = blogRepository.findTop10ByOrderByCreatedAtDesc();
        } else {
            blogs = blogRepository
                    .findTop10ByCreatedAtBeforeOrderByCreatedAtDesc(cursorCreatedAt);
        }

        Instant nextCursor = blogs.isEmpty()
                ? null
                : blogs.get(blogs.size() - 1).getCreatedAt();

        // 1️⃣ Extract blog IDs
        List<String> blogIds = blogs.stream()
                .map(Blog::getId)
                .toList();

        // 2️⃣ Fetch liked blog IDs (only if userId exists)
        Set<String> likedBlogIds =
                (userId == null || blogIds.isEmpty())
                        ? Set.of()
                        : new HashSet<>(
                        likesRepository.findLikedBlogIdsByUser(userId, blogIds)
                );

        // 3️⃣ Map to feed DTO
        List<FeedBlogDto> feedBlogs = blogs.stream()
                .map(blog -> new FeedBlogDto(
                        blog.getId(),
                        blog.getTitle(),
                        blog.getContent(),
                        blog.getLikes(),
                        likedBlogIds.contains(blog.getId()),
                        blog.getCreatedAt()
                ))
                .toList();

        return new FeedResponse(feedBlogs, nextCursor);
    }



    @Override
    public List<SearchResult> getSearch(String query) {
        List<SearchResult> results = new ArrayList<>();

        // -------- BLOG SEARCH (text search) --------
        TextCriteria blogCriteria = TextCriteria.forDefaultLanguage().matching(query);
        Query blogQuery = TextQuery.queryText(blogCriteria).sortByScore().limit(20);
        List<Blog> blogs = mongoTemplate.find(blogQuery, Blog.class);

        for (Blog blog : blogs) {
            results.add(new SearchResult(
                    "BLOG",
                    blog.getId(),
                    blog.getTitle(),
                    blog.getScore()
            ));
        }

        // -------- PROFILE SEARCH (regex for substring) --------
        Query profileQuery = new Query();
        profileQuery.addCriteria(Criteria.where("username").regex(".*" + query + ".*", "i"));
        profileQuery.limit(20);
        List<Profile> profiles = mongoTemplate.find(profileQuery, Profile.class);

        for (Profile profile : profiles) {
            results.add(new SearchResult(
                    "AUTHOR",
                    profile.getId(),
                    profile.getUsername(),
                    profile.getScore()
            ));
        }

        // Sort results by score (highest first)
        results.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        // Return top 10 results
        return results.stream().limit(10).toList();
    }

}

