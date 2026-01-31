package com.example.blog_backend.util;

import com.example.blog_backend.enums.ImageType;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageCompressor {

    public static byte[] compress(MultipartFile image , ImageType type) throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Thumbnails.of(image.getInputStream())
                .size(type.width, type.height)
                .outputQuality(type.quality)
                .outputFormat("jpg")
                .toOutputStream(out);

        return out.toByteArray();
    }
}
