package com.example.blog_backend.service;

import com.example.blog_backend.dto.CommentCreation;
import com.example.blog_backend.dto.CommentResponse;
import com.example.blog_backend.entity.Comments;
import com.example.blog_backend.entity.Likes;

import java.util.List;

public interface LikesCommentService {


    boolean createLikes(String userId, String blogId);

    void removeLikes(String userId, String blogId);



    CommentResponse createComment(String userId, CommentCreation content);

    void removeComment(String commentId, String userId);

    List<CommentResponse> getComments(String blogId, String userId);




}
