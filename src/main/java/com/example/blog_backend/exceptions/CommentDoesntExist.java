package com.example.blog_backend.exceptions;

public class CommentDoesntExist extends RuntimeException {
    public CommentDoesntExist(String message) {
        super(message);
    }
}
