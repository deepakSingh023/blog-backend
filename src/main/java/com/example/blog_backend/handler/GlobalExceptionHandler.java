package com.example.blog_backend.handler;


import com.example.blog_backend.dto.ErrorResponse;
import com.example.blog_backend.exceptions.*;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.access.AccessDeniedException;


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
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        System.out.println("🔴🔴🔴 GLOBAL EXCEPTION CAUGHT: " + ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + ex.getMessage());
    }

    @ExceptionHandler(BlogNotFound.class)
    public ResponseEntity<ErrorResponse> handleBlogNotFound(Exception ex){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("blog doesnt exist"));
    }

    @ExceptionHandler( AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(Exception ex){

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("you cannot access this blog"));
    }

    @ExceptionHandler(LikesNotFound.class)
    public ResponseEntity<ErrorResponse> handleLikeNotFound(Exception ex){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("like not found"));
    }

    @ExceptionHandler(ActionNotAllowed.class)
    public ResponseEntity<ErrorResponse> handleActionNotAllowed(Exception ex){

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("cannot delete other peoples like"));

    }

    @ExceptionHandler(CommentAlreadyExists.class)
    public ResponseEntity<ErrorResponse> CommentAlreadyExists(Exception ex){

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body( new ErrorResponse("one comment already exists"));
    }

    @ExceptionHandler(CommentDoesntExist.class)
    public ResponseEntity<ErrorResponse> CommentDoesntExists(Exception ex){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body( new ErrorResponse(" comment doesnt exists"));
    }


}
