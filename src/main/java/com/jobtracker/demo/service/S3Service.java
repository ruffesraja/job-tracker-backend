package com.jobtracker.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String uploadFile(MultipartFile file);
    String getPresignedUrl(String key);
}
