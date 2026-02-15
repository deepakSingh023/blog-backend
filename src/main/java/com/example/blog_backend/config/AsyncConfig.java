package com.example.blog_backend.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class AsyncConfig {

    @Bean("blogExecutor")
    public Executor blogExecutor(){

        ThreadPoolTaskExecutor thread = new ThreadPoolTaskExecutor();

        thread.setCorePoolSize(10);
        thread.setMaxPoolSize(20);
        thread.setQueueCapacity(100);
        thread.setThreadNamePrefix("blog-");
        thread.setRejectedExecutionHandler(
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        thread.initialize();
        return thread;
    }

    @Bean("decIncExecutor")
    public Executor likeAndCommentExecutor(){

        ThreadPoolTaskExecutor thread = new ThreadPoolTaskExecutor();

        thread.setCorePoolSize(20);
        thread.setMaxPoolSize(20);
        thread.setQueueCapacity(100);
        thread.setThreadNamePrefix("likeAndComment-");
        thread.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        thread.initialize();
        return thread;
    }



}
