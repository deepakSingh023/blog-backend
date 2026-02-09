package com.example.blog_backend.exceptions;

public class CommentAlreadyExists extends RuntimeException {
    public CommentAlreadyExists(String message) {
        super(message);
    }
}
