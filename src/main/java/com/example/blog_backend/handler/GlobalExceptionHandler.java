package com.example.blog_backend.handler;


import com.example.blog_backend.dto.ErrorResponse;
import com.example.blog_backend.exceptions.UserAlreadyExistsException;
import com.example.blog_backend.exceptions.UserDoesntExist;
import com.example.blog_backend.exceptions.WrongPassword;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(
            UserAlreadyExistsException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)   // 409
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(UserDoesntExist.class)
    public ResponseEntity<ErrorResponse> handleUserDoesntExist(UserDoesntExist ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(WrongPassword.class)
    public ResponseEntity<ErrorResponse> handleWrongPassword(WrongPassword ex){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                .body(new ErrorResponse("Internal server error"));
    }
}
