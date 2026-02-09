package com.example.blog_backend.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImageType {
    BLOG(1200,675,0.85),
    PROFILE(512,512,0.7);

    public final int width;
    public final int height;
    public final double quality;


}
