package com.example.blog_backend.exceptions;

public class ActionNotAllowed extends RuntimeException {
    public ActionNotAllowed(String message) {
        super(message);
    }
}
