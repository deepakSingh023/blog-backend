package com.example.blog_backend.service;


import com.example.blog_backend.dto.FeedResponse;
import com.example.blog_backend.dto.SearchResult;
import com.example.blog_backend.entity.Blog;
import com.example.blog_backend.entity.Profile;
import com.example.blog_backend.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;


import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final MongoTemplate mongoTemplate;

    private final BlogRepository blogRepository;


    @Override
    public FeedResponse getFeed(Instant cursorCreatedAt) {

        List<Blog> blogs;

        if (cursorCreatedAt == null) {
            // first page
            blogs = blogRepository.findTop10ByOrderByCreatedAtDesc();
        } else {
            // next page
            blogs = blogRepository.findTop10ByCreatedAtBeforeOrderByCreatedAtDesc(cursorCreatedAt);
        }

        // determine next cursor
        Instant nextCursor = null;
        if (!blogs.isEmpty()) {
            nextCursor = blogs.get(blogs.size() - 1).getCreatedAt();
        }

        return new FeedResponse(blogs, nextCursor);
    }



    @Override
    public List<SearchResult> getSearch(String query) {

        List<SearchResult> results = new ArrayList<>();

        // -------- BLOG SEARCH --------
        TextCriteria blogCriteria =
                TextCriteria.forDefaultLanguage().matching(query);

        Query blogQuery = TextQuery.queryText(blogCriteria)
                .sortByScore()
                .limit(20);

        List<Blog> blogs = mongoTemplate.find(blogQuery, Blog.class);

        for (Blog blog : blogs) {
            results.add(new SearchResult(
                    "BLOG",
                    blog.getId(),
                    blog.getTitle(),
                    blog.getScore()
            ));
        }

        // -------- PROFILE SEARCH --------
        TextCriteria profileCriteria =
                TextCriteria.forDefaultLanguage().matching(query);

        Query profileQuery = TextQuery.queryText(profileCriteria)
                .sortByScore()
                .limit(20);

        List<Profile> profiles = mongoTemplate.find(profileQuery, Profile.class);

        for (Profile profile : profiles) {
            results.add(new SearchResult(
                    "AUTHOR",
                    profile.getId(),
                    profile.getUsername(),
                    profile.getScore()
            ));
        }

        // -------- MERGE & SORT --------
        results.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        return results.stream()
                .limit(10)
                .toList();
    }
}

