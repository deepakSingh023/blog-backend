package com.example.blog_backend.service;

import com.example.blog_backend.dto.CommentCreation;
import com.example.blog_backend.entity.Comments;
import com.example.blog_backend.entity.Likes;

public interface LikesCommentService {


    boolean createLikes(String userId, String blogId);

    void removeLikes(String userId, String blogId);



    Comments createComment(String userId, CommentCreation content);

    void removeComment(String commentId, String userId);


}
