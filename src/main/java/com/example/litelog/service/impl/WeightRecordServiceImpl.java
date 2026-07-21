package com.example.litelog.service.impl;

import com.example.litelog.dto.request.WeightRecordRequest;
import com.example.litelog.dto.request.WeightRecordSyncRequest;
import com.example.litelog.dto.response.WeightRecordSyncResponse;
import com.example.litelog.entity.WeightRecord;
import com.example.litelog.exception.BusinessException;
import com.example.litelog.repository.WeightRecordRepository;
import com.example.litelog.service.UserIdentifierService;
import com.example.litelog.service.WeightRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WeightRecordServiceImpl implements WeightRecordService {

    private static final Logger log = LoggerFactory.getLogger(WeightRecordServiceImpl.class);

    private final WeightRecordRepository weightRecordRepository;
    private final UserIdentifierService userIdentifierService;

    public WeightRecordServiceImpl(WeightRecordRepository weightRecordRepository, UserIdentifierService userIdentifierService) {
        this.weightRecordRepository = weightRecordRepository;
        this.userIdentifierService = userIdentifierService;
    }

    @Value("${record.image.upload.path:./uploads/records}")
    private String imageUploadPath;

    @Value("${record.image.base.url:http://localhost:8080/api/record-images}")
    private String imageBaseUrl;

    private Long getUserId(String userId, String idType) {
        return userIdentifierService.getOrCreateUserId(userId, idType);
    }

    private LocalDateTime toLocalDateTime(Long epochSecond) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneId.systemDefault());
    }

    @Override
    @Transactional
    public WeightRecordSyncResponse syncRecords(String userId, String idType, WeightRecordSyncRequest request) {
        try {
            Long userIdValue = getUserId(userId, idType);
            List<String> syncedRecordIds = new ArrayList<>();
            List<String> conflictRecordIds = new ArrayList<>();

            // 批量查询所有已存在的记录，消除 N+1 查询
            List<String> recordIds = request.getRecords().stream()
                    .map(WeightRecordRequest::getRecordId)
                    .toList();
            Map<String, WeightRecord> existingRecordMap = weightRecordRepository.findAllByRecordIdIn(recordIds)
                    .stream()
                    .collect(Collectors.toMap(WeightRecord::getRecordId, r -> r));

            for (WeightRecordRequest recordRequest : request.getRecords()) {
                try {
                    if (Boolean.TRUE.equals(recordRequest.getDeleted())) {
                        WeightRecord existingRecord = existingRecordMap.get(recordRequest.getRecordId());
                        if (existingRecord != null) {
                            deleteRecordImage(existingRecord.getImageUrl());
                            weightRecordRepository.delete(existingRecord);
                            log.info("删除体重记录：{}", recordRequest.getRecordId());
                        }
                        syncedRecordIds.add(recordRequest.getRecordId());
                    } else {
                        WeightRecord existingRecord = existingRecordMap.get(recordRequest.getRecordId());
                        if (existingRecord != null) {
                            if (updateRecordWithConflictCheck(recordRequest, existingRecord)) {
                                syncedRecordIds.add(recordRequest.getRecordId());
                            } else {
                                conflictRecordIds.add(recordRequest.getRecordId());
                            }
                        } else {
                            createRecord(recordRequest, userIdValue);
                            syncedRecordIds.add(recordRequest.getRecordId());
                        }
                    }
                } catch (Exception e) {
                    log.warn("同步记录失败：{}, 错误：{}", recordRequest.getRecordId(), e.getMessage());
                }
            }

            String message = "同步成功";
            if (!conflictRecordIds.isEmpty()) {
                message = String.format("同步成功，%d 条记录存在冲突已跳过", conflictRecordIds.size());
            }
            log.info("体重记录同步完成，userId={}, idType={}, 成功同步 {} 条，冲突 {} 条", 
                    userId, idType, syncedRecordIds.size(), conflictRecordIds.size());

            return WeightRecordSyncResponse.builder()
                    .success(true)
                    .message(message)
                    .syncedCount(syncedRecordIds.size())
                    .syncedRecordIds(syncedRecordIds)
                    .conflictRecordIds(conflictRecordIds)
                    .build();

        } catch (Exception e) {
            log.error("同步体重记录失败：userId={}, idType={}, error={}", userId, idType, e.getMessage());
            return WeightRecordSyncResponse.builder()
                    .success(false)
                    .message("同步失败，请重试")
                    .syncedCount(0)
                    .syncedRecordIds(new ArrayList<>())
                    .conflictRecordIds(new ArrayList<>())
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
                .hipCircumference(request.getHipCircumference())
                .chestCircumference(request.getChestCircumference())
                .thighCircumference(request.getThighCircumference())
                .measurementTimePeriod(request.getMeasurementTimePeriod())
                .note(request.getNote())
                .date(toLocalDateTime(request.getDate()))
                .createdAt(toLocalDateTime(request.getCreatedAt()))
                .updatedAt(toLocalDateTime(request.getUpdatedAt()))
                .build();

        weightRecordRepository.save(record);
    }

    private boolean updateRecordWithConflictCheck(WeightRecordRequest request, WeightRecord existingRecord) {
        LocalDateTime serverUpdatedAt = existingRecord.getUpdatedAt();
        LocalDateTime clientUpdatedAt = toLocalDateTime(request.getUpdatedAt());

        if (serverUpdatedAt.isAfter(clientUpdatedAt)) {
            log.info("记录存在冲突，跳过更新：recordId={}, serverUpdatedAt={}, clientUpdatedAt={}", 
                    request.getRecordId(), serverUpdatedAt, clientUpdatedAt);
            return false;
        }

        updateRecordFields(existingRecord, request);

        if (Boolean.TRUE.equals(request.getDeleteImage())) {
            deleteRecordImage(existingRecord.getImageUrl());
            existingRecord.setImageUrl(null);
        }
        
        weightRecordRepository.save(existingRecord);
        return true;
    }

    private void updateRecordFields(WeightRecord existingRecord, WeightRecordRequest request) {
        existingRecord.setWeight(request.getWeight());
        existingRecord.setBodyFatPercentage(request.getBodyFatPercentage());
        existingRecord.setWaistCircumference(request.getWaistCircumference());
        existingRecord.setHipCircumference(request.getHipCircumference());
        existingRecord.setChestCircumference(request.getChestCircumference());
        existingRecord.setThighCircumference(request.getThighCircumference());
        existingRecord.setMeasurementTimePeriod(request.getMeasurementTimePeriod());
        existingRecord.setNote(request.getNote());
        existingRecord.setDate(toLocalDateTime(request.getDate()));
        existingRecord.setUpdatedAt(toLocalDateTime(request.getUpdatedAt()));
    }

    @Override
    @Transactional
    public WeightRecordSyncResponse syncRecordsWithImages(String userId, String idType, WeightRecordSyncRequest request, List<MultipartFile> files) {
        try {
            Long userIdValue = getUserId(userId, idType);
            ensureUploadDirectoryExists();
            Map<String, MultipartFile> fileMap = buildFileMap(files);
            List<String> syncedRecordIds = new ArrayList<>();
            List<WeightRecordSyncResponse.SyncedRecord> syncedRecords = new ArrayList<>();

            // 批量查询所有已存在的记录，消除 N+1 查询
            List<String> recordIds = request.getRecords().stream()
                    .map(WeightRecordRequest::getRecordId)
                    .toList();
            Map<String, WeightRecord> existingRecordMap = weightRecordRepository.findAllByRecordIdIn(recordIds)
                    .stream()
                    .collect(Collectors.toMap(WeightRecord::getRecordId, r -> r));

            for (WeightRecordRequest recordRequest : request.getRecords()) {
                try {
                    String imageUrl = null;
                    if (Boolean.TRUE.equals(recordRequest.getDeleted())) {
                        WeightRecord existingRecord = existingRecordMap.get(recordRequest.getRecordId());
                        if (existingRecord != null) {
                            deleteRecordImage(existingRecord.getImageUrl());
                            weightRecordRepository.delete(existingRecord);
                            log.info("删除体重记录：{}", recordRequest.getRecordId());
                        }
                    } else {
                        WeightRecord existingRecord = existingRecordMap.get(recordRequest.getRecordId());
                        if (existingRecord != null) {
                            imageUrl = updateRecordWithImage(recordRequest, existingRecord, fileMap);
                        } else {
                            imageUrl = createRecordWithImage(recordRequest, fileMap, userIdValue);
                        }
                    }
                    syncedRecordIds.add(recordRequest.getRecordId());
                    if (imageUrl != null) {
                        syncedRecords.add(new WeightRecordSyncResponse.SyncedRecord(recordRequest.getRecordId(), imageUrl));
                    }
                } catch (Exception e) {
                    log.warn("同步记录失败：{}, 错误：{}", recordRequest.getRecordId(), e.getMessage());
                }
            }

            log.info("体重记录同步完成(含图片)，userId={}, idType={}, 成功同步 {} 条", userId, idType, syncedRecordIds.size());

            return WeightRecordSyncResponse.builder()
                    .success(true)
                    .message("同步成功")
                    .syncedCount(syncedRecordIds.size())
                    .syncedRecordIds(syncedRecordIds)
                    .syncedRecords(syncedRecords)
                    .build();

        } catch (Exception e) {
            log.error("同步体重记录失败：userId={}, idType={}, error={}", userId, idType, e.getMessage());
            return WeightRecordSyncResponse.builder()
                    .success(false)
                    .message("同步失败，请重试")
                    .syncedCount(0)
                    .syncedRecordIds(new ArrayList<>())
                    .build();
        }
    }

    private String createRecordWithImage(WeightRecordRequest request, Map<String, MultipartFile> fileMap, Long userId) throws IOException {
        WeightRecord record = WeightRecord.builder()
                .userId(userId)
                .recordId(request.getRecordId())
                .weight(request.getWeight())
                .bodyFatPercentage(request.getBodyFatPercentage())
                .waistCircumference(request.getWaistCircumference())
                .hipCircumference(request.getHipCircumference())
                .chestCircumference(request.getChestCircumference())
                .thighCircumference(request.getThighCircumference())
                .measurementTimePeriod(request.getMeasurementTimePeriod())
                .note(request.getNote())
                .date(toLocalDateTime(request.getDate()))
                .createdAt(toLocalDateTime(request.getCreatedAt()))
                .updatedAt(toLocalDateTime(request.getUpdatedAt()))
                .build();

        String imageFileName = request.getImageFileName();
        if (imageFileName != null && !imageFileName.isEmpty() && fileMap.containsKey(imageFileName)) {
            String imageUrl = saveImage(fileMap.get(imageFileName), request.getRecordId());
            record.setImageUrl(imageUrl);
        }

        weightRecordRepository.save(record);
        return record.getImageUrl();
    }

    private String updateRecordWithImage(WeightRecordRequest request, WeightRecord existingRecord, Map<String, MultipartFile> fileMap) throws IOException {
        updateRecordFields(existingRecord, request);

        String imageFileName = request.getImageFileName();
        if (imageFileName != null && !imageFileName.isEmpty() && fileMap.containsKey(imageFileName)) {
            deleteRecordImage(existingRecord.getImageUrl());
            String imageUrl = saveImage(fileMap.get(imageFileName), request.getRecordId());
            existingRecord.setImageUrl(imageUrl);
        }

        weightRecordRepository.save(existingRecord);
        return existingRecord.getImageUrl();
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
            throw new BusinessException("请上传有效的图片文件");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BusinessException("图片大小不能超过5MB");
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