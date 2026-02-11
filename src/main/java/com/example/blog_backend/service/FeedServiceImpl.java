package com.example.blog_backend.service;


import com.example.blog_backend.dto.*;
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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

        List<String> blogIds = blogs.stream()
                .map(blog -> blog.getId())
                .toList();

        Set<String> likedBlogIds =
                likesRepository.findLikedBlogIdsByUser(userId, blogIds)
                        .stream()
                        .map(LikedBlogIdProjection::getBlogId)
                        .collect(Collectors.toSet());



        List<FeedBlogDto> feedBlogs = blogs.stream()
                .map(blog -> new FeedBlogDto(
                        blog.getId(),
                        blog.getTitle(),
                        blog.getContent(),
                        blog.getImage(),
                        blog.getLikes(),
                        blog.getComments(),
                        blog.getUsername(),
                        blog.getUserImage(),
                        likedBlogIds.contains(blog.getId()),
                        blog.getCreatedAt()
                ))
                .toList();

        return new FeedResponse(feedBlogs, nextCursor);
    }


    @Override
    public List<SearchResult> getSearch(String query) {
        List<SearchResult> results = new ArrayList<>();

        // -------- BLOG SEARCH (TEXT SEARCH) --------
        TextCriteria blogCriteria = TextCriteria.forDefaultLanguage().matching(query);

        Query blogQuery = TextQuery.queryText(blogCriteria)
                .sortByScore()
                .limit(20);

        List<Blog> blogs = mongoTemplate.find(blogQuery, Blog.class);

        for (Blog blog : blogs) {
            double score = blog.getScore() != null ? blog.getScore() : 0.0;

            SearchResult result = new SearchResult(
                    "BLOG",
                    blog.getId(),
                    blog.getTitle(),
                    null,
                    score
            );
            results.add(result);
        }

        // -------- PROFILE SEARCH (REGEX SEARCH) --------
        Query profileQuery = new Query();
        profileQuery.addCriteria(
                Criteria.where("username")
                        .regex(".*" + Pattern.quote(query) + ".*", "i")
        );
        profileQuery.limit(20);

        List<Profile> profiles = mongoTemplate.find(profileQuery, Profile.class);

        for (Profile profile : profiles) {
            SearchResult result = new SearchResult(
                    "AUTHOR",
                    profile.getId(),
                    profile.getUsername(),
                    profile.getUsername(),
                    0.5
            );
            results.add(result);
        }

        // -------- SORT + LIMIT --------
        results.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        return results.stream().limit(10).toList();
    }
}
