package com.example.litelog.controller;

import com.example.litelog.dto.request.WeightRecordSyncRequest;
import com.example.litelog.dto.response.WeightRecordSyncResponse;
import com.example.litelog.service.WeightRecordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/weight")
@RequiredArgsConstructor
public class WeightRecordController {

    private final WeightRecordService weightRecordService;
    private final ObjectMapper objectMapper;

    @PostMapping("/sync")
    public ResponseEntity<WeightRecordSyncResponse> syncRecords(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-Id-Type", required = false, defaultValue = "device") String idType,
            @Valid @RequestBody WeightRecordSyncRequest request) {
        
        log.info("同步体重记录: userId={}, idType={}, recordCount={}", userId, idType, request.getRecords().size());
        
        WeightRecordSyncResponse response = weightRecordService.syncRecords(userId, idType, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/sync-with-images", consumes = "multipart/form-data")
    public ResponseEntity<WeightRecordSyncResponse> syncRecordsWithImages(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-Id-Type", required = false, defaultValue = "device") String idType,
            @RequestParam("records") String recordsJson,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        
        try {
            WeightRecordSyncRequest request = objectMapper.readValue(recordsJson, WeightRecordSyncRequest.class);
            
            log.info("同步体重记录(含图片): userId={}, idType={}, recordCount={}, fileCount={}", 
                    userId, idType, request.getRecords().size(), files != null ? files.size() : 0);
            
            WeightRecordSyncResponse response = weightRecordService.syncRecordsWithImages(userId, idType, request, files);
            return ResponseEntity.ok(response);
            
        } catch (JsonProcessingException e) {
            log.error("解析记录JSON失败：{}", e.getMessage());
            return ResponseEntity.ok(WeightRecordSyncResponse.builder()
                    .success(false)
                    .message("数据格式错误")
                    .syncedCount(0)
                    .build());
        }
    }
}