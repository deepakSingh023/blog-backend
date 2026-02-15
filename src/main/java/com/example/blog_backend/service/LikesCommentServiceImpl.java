package com.example.blog_backend.service;

import com.example.blog_backend.dto.CommentCreation;
import com.example.blog_backend.dto.CommentResponse;
import com.example.blog_backend.entity.Blog;
import com.example.blog_backend.entity.Comments;
import com.example.blog_backend.entity.Likes;
import com.example.blog_backend.entity.Profile;
import com.example.blog_backend.exceptions.*;
import com.example.blog_backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

//transactions won't work because mongo free tier doesn't  support transactions and the db need to be SQL
@Service
@RequiredArgsConstructor
public class LikesCommentServiceImpl implements LikesCommentService{


    Logger log = LoggerFactory.getLogger(LikesCommentServiceImpl.class);

    private final LikesRepository likesRepository;

    private final CommentRepository commentRepository;

    private final AuthRepository authRepository;
    private final BlogRepository blogRepository;

    private final ProfileRepository profileRepository;

    private final IncrementRepository incrementRepository;

    @Override
    @Transactional
    public boolean createLikes(String userId,String blogId){

        if (likesRepository.existsByUserIdAndBlogId(userId, blogId)) {
            throw new LikeAlreadyExist("like already exists");
        }

        if(!blogRepository.existsById(blogId)){
            throw new BlogNotFound("blog doesn't exist");
        }


        Likes like = Likes.builder()
                .userId(userId)
                .blogId(blogId)
                .createdAt(Instant.now())
                .build();

        likesRepository.save(like);

        long start = System.currentTimeMillis();


        incrementRepository.likesIncrement(blogId);

        log.info("api = increment  latencyMs={}",System.currentTimeMillis()-start);

        return true;

    }



    @Override
    @Transactional
    public void removeLikes(String userId, String blogId){

        Likes like = likesRepository.findByUserIdAndBlogId(userId,blogId)
                .orElseThrow(()-> new LikesNotFound("likes not found"));


        if(!like.getUserId().equals(userId)){
            throw new ActionNotAllowed("Not authorized to delete this like");

        }

        likesRepository.delete(like);

        long start = System.currentTimeMillis();

        incrementRepository.likesDecrement(blogId);

        log.info("api = decrease latencyMs={}",System.currentTimeMillis()-start);

    }


    @Override
    @Transactional
    public CommentResponse createComment(String userId, CommentCreation content){

        UUID userUuid;
        try{
            userUuid = UUID.fromString(userId);
        }catch(IllegalArgumentException ex){
            throw new IllegalArgumentException("user id is wrong");
        }

        if(!authRepository.existsById(userUuid)){
            throw new UserDoesntExist("user doesnt exist");
        }

        if(commentRepository.existsByUserIdAndBlogId(userId , content.blogId())){
            throw new CommentAlreadyExists("cannot create multipel comment for a single post by same user");

        }

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(()-> new UserDoesntExist("user doesnt exist"));

        Comments comment = Comments.builder()
                .userId(userId)
                .blogId(content.blogId())
                .content(content.content())
                .username(profile.getUsername())
                .userImage(profile.getProfilePic())
                .createdAt(Instant.now())
                .build();

        commentRepository.save(comment);

        incrementRepository.commentIncrement(content.blogId());

        CommentResponse comm = new CommentResponse(
                comment.getId(),
                comment.getUserId(),
                comment.getBlogId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUsername(),
                comment.getUserImage(),
                false,
                true
        );

        return comm;

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

        comment.setIsDeleted(true);
        commentRepository.save(comment);

        incrementRepository.commentDecrement(blogId);
    }

    @Override
    public List<CommentResponse> getComments(String blogId, String userId) {

        if (!blogRepository.existsById(blogId)) {
            throw new BlogNotFound("blog doesn't exist");
        }

        return commentRepository
                .findTop10ByBlogIdOrderByCreatedAtDesc(blogId)
                .stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getUserId(),
                        comment.getBlogId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getUsername(),
                        comment.getUserImage(),
                        comment.isIsDeleted(),
                        comment.getUserId().equals(userId)
                ))
                .toList();
    }




}
