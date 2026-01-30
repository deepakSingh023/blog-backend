package com.example.blog_backend.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageCompressor {

    public static byte[] compress(MultipartFile image) throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Thumbnails.of(image.getInputStream())
                .size(512,512)
                .outputQuality(0.7)
                .outputFormat("jpg")
                .toOutputStream(out);

        return out.toByteArray();
    }
}
