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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeightRecordServiceImpl implements WeightRecordService {

    private final WeightRecordRepository weightRecordRepository;
    private final UserRepository userRepository;

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
                        java.time.Instant.ofEpochMilli(request.getDate()),
                        ZoneId.systemDefault()))
                .createdAt(LocalDateTime.ofInstant(
                        java.time.Instant.ofEpochMilli(request.getCreatedAt()),
                        ZoneId.systemDefault()))
                .updatedAt(LocalDateTime.ofInstant(
                        java.time.Instant.ofEpochMilli(request.getUpdatedAt()),
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
                    java.time.Instant.ofEpochMilli(request.getDate()),
                    ZoneId.systemDefault()));
            existingRecord.setUpdatedAt(LocalDateTime.ofInstant(
                    java.time.Instant.ofEpochMilli(request.getUpdatedAt()),
                    ZoneId.systemDefault()));
            weightRecordRepository.save(existingRecord);
        });
    }

    private void deleteRecord(String recordId) {
        weightRecordRepository.findByRecordId(recordId).ifPresent(existingRecord -> {
            weightRecordRepository.delete(existingRecord);
            log.info("删除体重记录：{}", recordId);
        });
    }
}