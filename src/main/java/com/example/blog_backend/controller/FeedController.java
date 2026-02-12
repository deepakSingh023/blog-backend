package com.example.blog_backend.controller;


import com.example.blog_backend.dto.FeedResponse;
import com.example.blog_backend.dto.InfoResponse;
import com.example.blog_backend.dto.SearchResult;
import com.example.blog_backend.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/map")
public class FeedController {

    private final FeedService feedService;


    @GetMapping("/feed")
    public FeedResponse getFeed(
            @RequestParam(required = false) Instant cursor,
            @RequestParam(required = false) String userId

    ) {
        return feedService.getFeed(cursor, userId);
    }



    @GetMapping("/search")
    public ResponseEntity<List<SearchResult>> getSearch(
            @RequestParam(required = true) String query
    ){

        List<SearchResult> result = feedService.getSearch(query);

        return ResponseEntity.ok(result);


    }

    @GetMapping("/userInfo")
    public ResponseEntity<InfoResponse> getInfo(
            @RequestParam(required = true) String userId
    ){

        InfoResponse response = feedService.getInfo(userId);

        return ResponseEntity.ok(response);



    }
}
