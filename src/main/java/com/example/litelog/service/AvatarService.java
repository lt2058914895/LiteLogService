package com.example.litelog.service;

import com.example.litelog.dto.response.AvatarUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AvatarService {
    AvatarUploadResponse uploadAvatar(String userId, MultipartFile file);
}