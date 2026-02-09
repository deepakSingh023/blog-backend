package com.example.blog_backend.exceptions;

public class BlogNotFound extends RuntimeException {
    public BlogNotFound(String message) {
        super(message);
    }
}
