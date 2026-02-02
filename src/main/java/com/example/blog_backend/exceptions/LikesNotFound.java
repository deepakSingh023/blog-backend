package com.example.blog_backend.exceptions;

public class LikesNotFound extends RuntimeException {
    public LikesNotFound(String message) {
        super(message);
    }
}
