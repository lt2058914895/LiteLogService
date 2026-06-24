package com.example.litelog.service.impl;

import com.example.litelog.dto.response.AvatarUploadResponse;
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
public class AvatarServiceImpl implements AvatarService {

    @Value("${avatar.upload.path}")
    private String uploadPath;

    @Value("${avatar.base.url}")
    private String baseUrl;

    @Override
    @Transactional
    public AvatarUploadResponse uploadAvatar(MultipartFile file) {
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

            // 生成唯一文件名
            String fileName = "avatar_" + UUID.randomUUID().toString().substring(0, 16) + ".jpg";
            Path filePath = uploadDir.resolve(fileName);

            // 保存文件
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 构建访问URL
            String avatarUrl = baseUrl + "/" + fileName;

            log.info("头像上传成功，URL：{}", avatarUrl);

            return AvatarUploadResponse.builder()
                    .success(true)
                    .message("上传成功")
                    .avatarUrl(avatarUrl)
                    .build();

        } catch (IOException e) {
            log.error("头像上传失败：{}", e.getMessage());
            return AvatarUploadResponse.builder()
                    .success(false)
                    .message("上传失败，请重试")
                    .build();
        }
    }
}
