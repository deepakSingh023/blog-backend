package com.example.blog_backend.exceptions;

public class LikeAlreadyExist extends RuntimeException {
    public LikeAlreadyExist(String message) {
        super(message);
    }
}
