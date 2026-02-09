package com.example.blog_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchResult {

    private String type;
    private String id;
    private String titleOrName;
    private Double score;
}
