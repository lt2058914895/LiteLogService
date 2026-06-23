package com.example.litelog.service.impl;

import com.example.litelog.dto.request.WeightRecordRequest;
import com.example.litelog.dto.request.WeightRecordSyncRequest;
import com.example.litelog.dto.response.WeightRecordSyncResponse;
import com.example.litelog.entity.WeightRecord;
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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeightRecordServiceImpl implements WeightRecordService {

    private final WeightRecordRepository weightRecordRepository;

    @Value("${record.image.upload.path:./uploads/records}")
    private String imageUploadPath;

    @Value("${record.image.base.url:http://localhost:8080/api/record-images}")
    private String imageBaseUrl;

    @Override
    @Transactional
    public WeightRecordSyncResponse syncRecords(WeightRecordSyncRequest request) {
        try {
            List<String> syncedRecordIds = new ArrayList<>();

            for (WeightRecordRequest recordRequest : request.getRecords()) {
                try {
                    if (Boolean.TRUE.equals(recordRequest.getDeleted())) {
                        deleteRecord(recordRequest.getRecordId());
                    } else if (weightRecordRepository.existsByRecordId(recordRequest.getRecordId())) {
                        updateRecord(recordRequest);
                    } else {
                        createRecord(recordRequest);
                    }
                    syncedRecordIds.add(recordRequest.getRecordId());
                } catch (Exception e) {
                    log.warn("同步记录失败：{}, 错误：{}", recordRequest.getRecordId(), e.getMessage());
                }
            }

            log.info("体重记录同步完成，成功同步 {} 条", syncedRecordIds.size());

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

    private void createRecord(WeightRecordRequest request) {
        WeightRecord record = WeightRecord.builder()
                .recordId(request.getRecordId())
                .weight(request.getWeight())
                .bodyFatPercentage(request.getBodyFatPercentage())
                .waistCircumference(request.getWaistCircumference())
                .hipCircumference(request.getHipCircumference())
                .chestCircumference(request.getChestCircumference())
                .thighCircumference(request.getThighCircumference())
                .measurementTimePeriod(request.getMeasurementTimePeriod())
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

    private void updateRecord(WeightRecordRequest request) {
        weightRecordRepository.findByRecordId(request.getRecordId()).ifPresent(existingRecord -> {
            existingRecord.setWeight(request.getWeight());
            existingRecord.setBodyFatPercentage(request.getBodyFatPercentage());
            existingRecord.setWaistCircumference(request.getWaistCircumference());
            existingRecord.setHipCircumference(request.getHipCircumference());
            existingRecord.setChestCircumference(request.getChestCircumference());
            existingRecord.setThighCircumference(request.getThighCircumference());
            existingRecord.setMeasurementTimePeriod(request.getMeasurementTimePeriod());
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
            deleteRecordImage(existingRecord.getImageUrl());
            weightRecordRepository.delete(existingRecord);
            log.info("删除体重记录：{}", recordId);
        });
    }

    @Override
    @Transactional
    public WeightRecordSyncResponse syncRecordsWithImages(WeightRecordSyncRequest request, List<MultipartFile> files) {
        try {
            ensureUploadDirectoryExists();
            Map<String, MultipartFile> fileMap = buildFileMap(files);
            List<String> syncedRecordIds = new ArrayList<>();

            for (WeightRecordRequest recordRequest : request.getRecords()) {
                try {
                    if (Boolean.TRUE.equals(recordRequest.getDeleted())) {
                        deleteRecord(recordRequest.getRecordId());
                    } else if (weightRecordRepository.existsByRecordId(recordRequest.getRecordId())) {
                        updateRecordWithImage(recordRequest, fileMap);
                    } else {
                        createRecordWithImage(recordRequest, fileMap);
                    }
                    syncedRecordIds.add(recordRequest.getRecordId());
                } catch (Exception e) {
                    log.warn("同步记录失败：{}, 错误：{}", recordRequest.getRecordId(), e.getMessage());
                }
            }

            log.info("体重记录同步完成(含图片)，成功同步 {} 条", syncedRecordIds.size());

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

    private void createRecordWithImage(WeightRecordRequest request, Map<String, MultipartFile> fileMap) throws IOException {
        WeightRecord record = WeightRecord.builder()
                .recordId(request.getRecordId())
                .weight(request.getWeight())
                .bodyFatPercentage(request.getBodyFatPercentage())
                .waistCircumference(request.getWaistCircumference())
                .hipCircumference(request.getHipCircumference())
                .chestCircumference(request.getChestCircumference())
                .thighCircumference(request.getThighCircumference())
                .measurementTimePeriod(request.getMeasurementTimePeriod())
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

        String imageFileName = request.getImageFileName();
        if (imageFileName != null && !imageFileName.isEmpty() && fileMap.containsKey(imageFileName)) {
            String imageUrl = saveImage(fileMap.get(imageFileName), request.getRecordId());
            record.setImageUrl(imageUrl);
        }

        weightRecordRepository.save(record);
    }

    private void updateRecordWithImage(WeightRecordRequest request, Map<String, MultipartFile> fileMap) throws IOException {
        weightRecordRepository.findByRecordId(request.getRecordId()).ifPresent(existingRecord -> {
            existingRecord.setWeight(request.getWeight());
            existingRecord.setBodyFatPercentage(request.getBodyFatPercentage());
            existingRecord.setWaistCircumference(request.getWaistCircumference());
            existingRecord.setHipCircumference(request.getHipCircumference());
            existingRecord.setChestCircumference(request.getChestCircumference());
            existingRecord.setThighCircumference(request.getThighCircumference());
            existingRecord.setMeasurementTimePeriod(request.getMeasurementTimePeriod());
            existingRecord.setNote(request.getNote());
            existingRecord.setDate(LocalDateTime.ofInstant(
                    java.time.Instant.ofEpochSecond(request.getDate()),
                    ZoneId.systemDefault()));
            existingRecord.setUpdatedAt(LocalDateTime.ofInstant(
                    java.time.Instant.ofEpochSecond(request.getUpdatedAt()),
                    ZoneId.systemDefault()));

            String imageFileName = request.getImageFileName();
            try {
                if (imageFileName != null && !imageFileName.isEmpty() && fileMap.containsKey(imageFileName)) {
                    deleteRecordImage(existingRecord.getImageUrl());
                    String imageUrl = saveImage(fileMap.get(imageFileName), request.getRecordId());
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

    private String saveImage(MultipartFile file, String recordId) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("请上传有效的图片文件");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("图片大小不能超过5MB");
        }

        String extension = getFileExtension(contentType);
        String fileName = recordId + "_" + UUID.randomUUID().toString().substring(0, 8) + "." + extension;
        Path filePath = Paths.get(imageUploadPath).resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

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