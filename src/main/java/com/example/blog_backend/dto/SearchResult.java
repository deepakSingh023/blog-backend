package com.example.blog_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


@Data
@AllArgsConstructor
public class SearchResult {
    private String type;
    private String id;
    private String title;
    private String username;
    private Double score;
}