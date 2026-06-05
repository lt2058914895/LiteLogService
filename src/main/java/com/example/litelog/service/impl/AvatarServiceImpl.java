package com.example.litelog.service.impl;

import com.example.litelog.dto.response.AvatarUploadResponse;
import com.example.litelog.entity.User;
import com.example.litelog.entity.UserProfile;
import com.example.litelog.repository.UserProfileRepository;
import com.example.litelog.repository.UserRepository;
import com.example.litelog.service.AvatarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Value("${avatar.upload.path}")
    private String uploadPath;

    @Value("${avatar.base.url}")
    private String baseUrl;

    @Override
    @Transactional
    public AvatarUploadResponse uploadAvatar(String userId, MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file == null || file.isEmpty()) {
                return AvatarUploadResponse.builder()
                        .success(false)
                        .message("请选择要上传的图片")
                        .build();
            }

            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return AvatarUploadResponse.builder()
                        .success(false)
                        .message("请上传有效的图片文件")
                        .build();
            }

            // 检查文件大小（限制为5MB）
            if (file.getSize() > 5 * 1024 * 1024) {
                return AvatarUploadResponse.builder()
                        .success(false)
                        .message("图片大小不能超过5MB")
                        .build();
            }

            // 确保上传目录存在
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // 生成文件名：以 userId.jpg 命名
            String fileName = userId + ".jpg";
            Path filePath = uploadDir.resolve(fileName);

            // 保存文件（允许覆盖已存在的文件）
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 构建访问URL
            String avatarUrl = baseUrl + "/" + fileName;

            // 更新用户头像（在 User 表中）
            Long userIdLong = Long.parseLong(userId);
            userRepository.findById(userIdLong).ifPresent(user -> {
                // 删除旧头像文件
                deleteOldAvatar(user.getAvatarUrl());
                
                user.setAvatarUrl(avatarUrl);
                userRepository.save(user);
                log.info("用户头像更新成功：{}, URL：{}", userId, avatarUrl);
            });

            return AvatarUploadResponse.builder()
                    .success(true)
                    .message("上传成功")
                    .avatarUrl(avatarUrl)
                    .build();

        } catch (NumberFormatException e) {
            log.warn("无效的用户ID格式：{}", userId);
            return AvatarUploadResponse.builder()
                    .success(false)
                    .message("无效的用户ID")
                    .build();
        } catch (IOException e) {
            log.error("头像上传失败：{}", e.getMessage());
            return AvatarUploadResponse.builder()
                    .success(false)
                    .message("上传失败，请重试")
                    .build();
        }
    }

    private String getFileExtension(String contentType) {
        return switch (contentType) {
            case "image/jpeg", "image/jpg" -> "jpg";
            case "image/png" -> "png";
            case "image/gif" -> "gif";
            default -> "jpg";
        };
    }

    /**
     * 删除旧头像文件
     * @param oldAvatarUrl 旧头像URL
     */
    private void deleteOldAvatar(String oldAvatarUrl) {
        if (oldAvatarUrl == null || oldAvatarUrl.isEmpty()) {
            return;
        }
        
        try {
            // 从URL中提取文件名
            String fileName = oldAvatarUrl.substring(oldAvatarUrl.lastIndexOf("/") + 1);
            Path oldFilePath = Paths.get(uploadPath).resolve(fileName);
            
            if (Files.exists(oldFilePath)) {
                Files.delete(oldFilePath);
                log.info("旧头像文件已删除：{}", oldFilePath);
            }
        } catch (Exception e) {
            log.warn("删除旧头像文件失败：{}", e.getMessage());
        }
    }
}