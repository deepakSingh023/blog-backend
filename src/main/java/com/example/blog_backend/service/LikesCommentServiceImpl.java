package com.example.blog_backend.service;

import com.example.blog_backend.dto.CommentCreation;
import com.example.blog_backend.entity.Blog;
import com.example.blog_backend.entity.Comments;
import com.example.blog_backend.entity.Likes;
import com.example.blog_backend.exceptions.*;
import com.example.blog_backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class LikesCommentServiceImpl implements LikesCommentService{

    private final LikesRepository likesRepository;

    private final CommentRepository commentRepository;

    private final AuthRepository authRepository;
    private final BlogRepository blogRepository;

    private final IncrementRepository incrementRepository;

    @Override
    @Transactional
    public Likes createLikes(String userId,String blogId){

        if (likesRepository.existsByUserIdAndBlogId(userId, blogId)) {
            throw new LikeAlreadyExist("liek already exists");
        }


        UUID userUuid;
        try {
            userUuid = UUID.fromString(userId);
        }catch(IllegalArgumentException ex){
            throw new IllegalArgumentException("wrong user id");
        }

        if(!authRepository.existsById(userUuid)){
            throw new UserDoesntExist("user doesnt exist");
        }

        if(!blogRepository.existsById(blogId)){
            throw new BlogNotFound("blog doesnt exist");
        }

        Likes like = Likes.builder()
                .userId(userId)
                .blogId(blogId)
                .createdAt(Instant.now())
                .build();

        likesRepository.save(like);

        incrementRepository.likesIncrement(blogId);

        return like;

    }



    @Override
    @Transactional
    public void removeLikes(String userId, String likeId){

        Likes like = likesRepository.findById(likeId)
                .orElseThrow(()-> new LikesNotFound("likes not found"));

        String blogId = like.getBlogId();

        if(!like.getUserId().equals(userId)){
            throw new ActionNotAllowed("Not authorized to delete this like");

        }

        likesRepository.delete(like);
        incrementRepository.likesDecrement(blogId);

    }


    @Override
    @Transactional
    public Comments createComment(String userId, CommentCreation content){

        UUID userUuid;
        try{
            userUuid = UUID.fromString(userId);
        }catch(IllegalArgumentException ex){
            throw new IllegalArgumentException("user id is wrong");
        }

        if(!authRepository.existsById(userUuid)){
            throw new UserDoesntExist("user doesnt exist");
        }

        if(commentRepository.ExistsByUserIdAndBlogId(userId , content.blogId())){
            throw new CommentAlreadyExists("cannot create multipel comment for a single post by same user");

        }

        Comments comment = Comments.builder()
                .userId(userId)
                .blogId(content.blogId())
                .content(content.content())
                .createdAt(Instant.now())
                .build();

        commentRepository.save(comment);

        incrementRepository.commentIncrement(content.blogId());

        return comment;



    }


    @Override
    @Transactional
    public void removeComment(String commentId, String userId){

        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new CommentDoesntExist("comment doesnt exist"));

        if(!comment.getUserId().equals(userId)){
            throw new ActionNotAllowed("this action is not allowed as the userid fo blog and user doesnt match");
        }

        String blogId = comment.getBlogId();

        commentRepository.delete(comment);

        incrementRepository.commentDecrement(blogId);
    }



}
