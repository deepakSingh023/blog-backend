package com.example.blog_backend.repository;

import com.example.blog_backend.entity.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfileRepository extends MongoRepository<Profile,String> {

     Boolean existsByUserId(String userId);

     Optional<Profile> findByUserId(String userId);

}
