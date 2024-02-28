package com.project.webtoonzoa.service;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${defaultImage.path}")
    private String defaultImagePath;

    @Value("${upload.path}")
    private String uploadPath;

    private final S3Service s3Service;

    public String createImageName(MultipartFile file) throws IOException {
        return getImageName(file);
    }

    private String getImageName(MultipartFile file) throws IOException {
        if (file != null) {
            String originalFileName = UUID.randomUUID() + file.getOriginalFilename();
            return s3Service.upload(file, originalFileName);
        }
        return defaultImagePath;
    }
}
