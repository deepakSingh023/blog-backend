package com.example.blog_backend.service;


import com.cloudinary.Cloudinary;
import com.example.blog_backend.config.CloudinaryConfig;
import com.example.blog_backend.dto.CloudinaryUploadResult;
import com.example.blog_backend.enums.FolderType;
import com.example.blog_backend.enums.ImageType;
import com.example.blog_backend.util.ImageCompressor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageStorageService {

    private final Cloudinary cloudinary;

    public CloudinaryUploadResult uploadProfileImage(MultipartFile file , ImageType type, FolderType folder) {
        try {
            byte[] compressed = ImageCompressor.compress(file, type);

            Map<?, ?> result = cloudinary.uploader().upload(
                    compressed,
                    Map.of(
                            "folder", folder.type,
                            "resource_type", "image"
                    )
            );

            return new CloudinaryUploadResult(
                    result.get("secure_url").toString(),
                    result.get("public_id").toString()
            );

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    public void deleteImage(String publicId) {
        try {
            cloudinary.uploader().destroy(
                    publicId,
                    Map.of("resource_type", "image")
            );
        } catch (IOException e) {
            throw new RuntimeException("Image deletion failed", e);
        }
    }
}
