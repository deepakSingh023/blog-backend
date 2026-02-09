package com.example.blog_backend.enums;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FolderType {

        BLOG("blog-images"),
        PROFILE("profile-images");

        public final String type;
}
