package com.example.blog_backend.exceptions;

public class UserDoesntExist extends RuntimeException {
    public UserDoesntExist(String message) {
        super(message);
    }
}
