package com.example.litelog.controller;

import com.example.litelog.dto.request.WeightRecordSyncRequest;
import com.example.litelog.dto.response.WeightRecordSyncResponse;
import com.example.litelog.entity.User;
import com.example.litelog.repository.UserRepository;
import com.example.litelog.service.WeightRecordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/weight")
@RequiredArgsConstructor
public class WeightRecordController {

    private final WeightRecordService weightRecordService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @PostMapping("/sync")
    public ResponseEntity<WeightRecordSyncResponse> syncRecords(
            @Valid @RequestBody WeightRecordSyncRequest request,
            Authentication authentication) {
        
        String phone = authentication.getName();
        
        // 根据手机号获取用户ID
        Optional<User> userOptional = userRepository.findByPhone(phone);
        
        if (userOptional.isEmpty()) {
            log.warn("用户不存在：{}", phone);
            return ResponseEntity.ok(WeightRecordSyncResponse.builder()
                    .success(false)
                    .message("用户不存在")
                    .syncedCount(0)
                    .build());
        }

        Long userId = userOptional.get().getId();
        
        log.info("同步体重记录: userId={}, recordCount={}", userId, request.getRecords().size());
        
        WeightRecordSyncResponse response = weightRecordService.syncRecords(userId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/sync-with-images", consumes = "multipart/form-data")
    public ResponseEntity<WeightRecordSyncResponse> syncRecordsWithImages(
            @RequestParam("records") String recordsJson,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            Authentication authentication) {
        
        String phone = authentication.getName();
        
        Optional<User> userOptional = userRepository.findByPhone(phone);
        
        if (userOptional.isEmpty()) {
            log.warn("用户不存在：{}", phone);
            return ResponseEntity.ok(WeightRecordSyncResponse.builder()
                    .success(false)
                    .message("用户不存在")
                    .syncedCount(0)
                    .build());
        }

        Long userId = userOptional.get().getId();
        
        try {
            WeightRecordSyncRequest request = objectMapper.readValue(recordsJson, WeightRecordSyncRequest.class);
            
            log.info("同步体重记录(含图片): userId={}, recordCount={}, fileCount={}", 
                    userId, request.getRecords().size(), files != null ? files.size() : 0);
            
            WeightRecordSyncResponse response = weightRecordService.syncRecordsWithImages(userId, request, files);
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