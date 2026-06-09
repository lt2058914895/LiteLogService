package com.example.litelog.service.impl;

import com.example.litelog.dto.request.WeightRecordRequest;
import com.example.litelog.dto.request.WeightRecordSyncRequest;
import com.example.litelog.dto.response.WeightRecordSyncResponse;
import com.example.litelog.entity.User;
import com.example.litelog.entity.WeightRecord;
import com.example.litelog.repository.UserRepository;
import com.example.litelog.repository.WeightRecordRepository;
import com.example.litelog.service.WeightRecordService;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeightRecordServiceImpl implements WeightRecordService {

    private final WeightRecordRepository weightRecordRepository;
    private final UserRepository userRepository;

    @Value("${record.image.upload.path:./uploads/records}")
    private String imageUploadPath;

    @Value("${record.image.base.url:http://localhost:8080/api/record-images}")
    private String imageBaseUrl;

    @Override
    @Transactional
    public WeightRecordSyncResponse syncRecords(Long userId, WeightRecordSyncRequest request) {
        try {
            // 验证用户是否存在
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                log.warn("用户不存在：{}", userId);
                return WeightRecordSyncResponse.builder()
                        .success(false)
                        .message("用户不存在")
                        .syncedCount(0)
                        .syncedRecordIds(new ArrayList<>())
                        .build();
            }

            List<String> syncedRecordIds = new ArrayList<>();

            for (WeightRecordRequest recordRequest : request.getRecords()) {
                try {
                    // 如果标记为已删除，则删除云端记录
                    if (Boolean.TRUE.equals(recordRequest.getDeleted())) {
                        deleteRecord(recordRequest.getRecordId());
                    } else if (weightRecordRepository.existsByRecordId(recordRequest.getRecordId())) {
                        // 更新已存在的记录
                        updateRecord(recordRequest, userId);
                    } else {
                        // 创建新记录
                        createRecord(recordRequest, userId);
                    }
                    syncedRecordIds.add(recordRequest.getRecordId());
                } catch (Exception e) {
                    log.warn("同步记录失败：{}, 错误：{}", recordRequest.getRecordId(), e.getMessage());
                }
            }

            log.info("用户 {} 体重记录同步完成，成功同步 {} 条", userId, syncedRecordIds.size());

            return WeightRecordSyncResponse.builder()
                    .success(true)
                    .message("同步成功")
                    .syncedCount(syncedRecordIds.size())
                    .syncedRecordIds(syncedRecordIds)
                    .build();

        } catch (Exception e) {
            log.error("同步体重记录失败：{}", e.getMessage());
            return WeightRecordSyncResponse.builder()
                    .success(false)
                    .message("同步失败，请重试")
                    .syncedCount(0)
                    .syncedRecordIds(new ArrayList<>())
                    .build();
        }
    }

    private void createRecord(WeightRecordRequest request, Long userId) {
        WeightRecord record = WeightRecord.builder()
                .userId(userId)
                .recordId(request.getRecordId())
                .weight(request.getWeight())
                .bodyFatPercentage(request.getBodyFatPercentage())
                .waistCircumference(request.getWaistCircumference())
                .note(request.getNote())
                .date(LocalDateTime.ofInstant(
                        java.time.Instant.ofEpochSecond(request.getDate()),
                        ZoneId.systemDefault()))
                .createdAt(LocalDateTime.ofInstant(
                        java.time.Instant.ofEpochSecond(request.getCreatedAt()),
                        ZoneId.systemDefault()))
                .updatedAt(LocalDateTime.ofInstant(
                        java.time.Instant.ofEpochSecond(request.getUpdatedAt()),
                        ZoneId.systemDefault()))
                .build();

        weightRecordRepository.save(record);
    }

    private void updateRecord(WeightRecordRequest request, Long userId) {
        weightRecordRepository.findByRecordId(request.getRecordId()).ifPresent(existingRecord -> {
            existingRecord.setWeight(request.getWeight());
            existingRecord.setBodyFatPercentage(request.getBodyFatPercentage());
            existingRecord.setWaistCircumference(request.getWaistCircumference());
            existingRecord.setNote(request.getNote());
            existingRecord.setDate(LocalDateTime.ofInstant(
                    java.time.Instant.ofEpochSecond(request.getDate()),
                    ZoneId.systemDefault()));
            existingRecord.setUpdatedAt(LocalDateTime.ofInstant(
                    java.time.Instant.ofEpochSecond(request.getUpdatedAt()),
                    ZoneId.systemDefault()));
            weightRecordRepository.save(existingRecord);
        });
    }

    private void deleteRecord(String recordId) {
        weightRecordRepository.findByRecordId(recordId).ifPresent(existingRecord -> {
            // 删除关联的图片文件
            deleteRecordImage(existingRecord.getImageUrl());
            weightRecordRepository.delete(existingRecord);
            log.info("删除体重记录：{}", recordId);
        });
    }

    @Override
    @Transactional
    public WeightRecordSyncResponse syncRecordsWithImages(Long userId, WeightRecordSyncRequest request, List<MultipartFile> files) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                log.warn("用户不存在：{}", userId);
                return WeightRecordSyncResponse.builder()
                        .success(false)
                        .message("用户不存在")
                        .syncedCount(0)
                        .syncedRecordIds(new ArrayList<>())
                        .build();
            }

            // 确保上传目录存在
            ensureUploadDirectoryExists();

            // 构建文件映射：原始文件名 -> MultipartFile
            Map<String, MultipartFile> fileMap = buildFileMap(files);

            List<String> syncedRecordIds = new ArrayList<>();

            for (WeightRecordRequest recordRequest : request.getRecords()) {
                try {
                    if (Boolean.TRUE.equals(recordRequest.getDeleted())) {
                        deleteRecord(recordRequest.getRecordId());
                    } else if (weightRecordRepository.existsByRecordId(recordRequest.getRecordId())) {
                        updateRecordWithImage(recordRequest, userId, fileMap);
                    } else {
                        createRecordWithImage(recordRequest, userId, fileMap);
                    }
                    syncedRecordIds.add(recordRequest.getRecordId());
                } catch (Exception e) {
                    log.warn("同步记录失败：{}, 错误：{}", recordRequest.getRecordId(), e.getMessage());
                }
            }

            log.info("用户 {} 体重记录同步完成(含图片)，成功同步 {} 条", userId, syncedRecordIds.size());

            return WeightRecordSyncResponse.builder()
                    .success(true)
                    .message("同步成功")
                    .syncedCount(syncedRecordIds.size())
                    .syncedRecordIds(syncedRecordIds)
                    .build();

        } catch (Exception e) {
            log.error("同步体重记录失败：{}", e.getMessage());
            return WeightRecordSyncResponse.builder()
                    .success(false)
                    .message("同步失败，请重试")
                    .syncedCount(0)
                    .syncedRecordIds(new ArrayList<>())
                    .build();
        }
    }

    private void createRecordWithImage(WeightRecordRequest request, Long userId, Map<String, MultipartFile> fileMap) throws IOException {
        WeightRecord record = WeightRecord.builder()
                .userId(userId)
                .recordId(request.getRecordId())
                .weight(request.getWeight())
                .bodyFatPercentage(request.getBodyFatPercentage())
                .waistCircumference(request.getWaistCircumference())
                .note(request.getNote())
                .date(LocalDateTime.ofInstant(
                        java.time.Instant.ofEpochSecond(request.getDate()),
                        ZoneId.systemDefault()))
                .createdAt(LocalDateTime.ofInstant(
                        java.time.Instant.ofEpochSecond(request.getCreatedAt()),
                        ZoneId.systemDefault()))
                .updatedAt(LocalDateTime.ofInstant(
                        java.time.Instant.ofEpochSecond(request.getUpdatedAt()),
                        ZoneId.systemDefault()))
                .build();

        // 处理图片上传
        String imageFileName = request.getImageFileName();
        if (imageFileName != null && !imageFileName.isEmpty() && fileMap.containsKey(imageFileName)) {
            String imageUrl = saveImage(fileMap.get(imageFileName), userId, request.getRecordId());
            record.setImageUrl(imageUrl);
        }

        weightRecordRepository.save(record);
    }

    private void updateRecordWithImage(WeightRecordRequest request, Long userId, Map<String, MultipartFile> fileMap) throws IOException {
        weightRecordRepository.findByRecordId(request.getRecordId()).ifPresent(existingRecord -> {
            existingRecord.setWeight(request.getWeight());
            existingRecord.setBodyFatPercentage(request.getBodyFatPercentage());
            existingRecord.setWaistCircumference(request.getWaistCircumference());
            existingRecord.setNote(request.getNote());
            existingRecord.setDate(LocalDateTime.ofInstant(
                    java.time.Instant.ofEpochSecond(request.getDate()),
                    ZoneId.systemDefault()));
            existingRecord.setUpdatedAt(LocalDateTime.ofInstant(
                    java.time.Instant.ofEpochSecond(request.getUpdatedAt()),
                    ZoneId.systemDefault()));

            // 处理图片上传
            String imageFileName = request.getImageFileName();
            try {
                if (imageFileName != null && !imageFileName.isEmpty() && fileMap.containsKey(imageFileName)) {
                    // 删除旧图片
                    deleteRecordImage(existingRecord.getImageUrl());
                    // 保存新图片
                    String imageUrl = saveImage(fileMap.get(imageFileName), userId, request.getRecordId());
                    existingRecord.setImageUrl(imageUrl);
                }
            } catch (IOException e) {
                log.warn("更新记录图片失败：{}", e.getMessage());
            }

            weightRecordRepository.save(existingRecord);
        });
    }

    private void ensureUploadDirectoryExists() throws IOException {
        Path uploadDir = Paths.get(imageUploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
            log.info("创建记录图片上传目录：{}", uploadDir.toAbsolutePath());
        }
    }

    private Map<String, MultipartFile> buildFileMap(List<MultipartFile> files) {
        Map<String, MultipartFile> fileMap = new HashMap<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    fileMap.put(file.getOriginalFilename(), file);
                }
            }
        }
        return fileMap;
    }

    private String saveImage(MultipartFile file, Long userId, String recordId) throws IOException {
        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("请上传有效的图片文件");
        }

        // 检查文件大小（限制为5MB）
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("图片大小不能超过5MB");
        }

        // 生成文件名：userId_recordId_uuid.ext
        String extension = getFileExtension(contentType);
        String fileName = userId + "_" + recordId + "_" + UUID.randomUUID().toString().substring(0, 8) + "." + extension;
        Path filePath = Paths.get(imageUploadPath).resolve(fileName);

        // 保存文件
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 构建访问URL
        String imageUrl = imageBaseUrl + "/" + fileName;
        log.info("记录图片保存成功：{}", imageUrl);

        return imageUrl;
    }

    private String getFileExtension(String contentType) {
        return switch (contentType) {
            case "image/jpeg", "image/jpg" -> "jpg";
            case "image/png" -> "png";
            case "image/gif" -> "gif";
            default -> "jpg";
        };
    }

    private void deleteRecordImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        try {
            // 从URL中提取文件名
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            Path filePath = Paths.get(imageUploadPath).resolve(fileName);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("记录图片已删除：{}", filePath);
            }
        } catch (Exception e) {
            log.warn("删除记录图片失败：{}", e.getMessage());
        }
    }
}