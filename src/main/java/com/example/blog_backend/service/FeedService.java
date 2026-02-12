package com.example.blog_backend.service;

import com.example.blog_backend.dto.FeedResponse;
import com.example.blog_backend.dto.InfoResponse;
import com.example.blog_backend.dto.SearchResult;
import com.example.blog_backend.entity.Blog;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

public interface FeedService {


    FeedResponse getFeed(Instant cursorCreatedAt, String userId);

    List<SearchResult> getSearch(String query);

    InfoResponse getInfo(String userId);
}
