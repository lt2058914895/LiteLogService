package com.example.litelog.controller;

import com.example.litelog.dto.request.WeightRecordSyncRequest;
import com.example.litelog.dto.response.WeightRecordSyncResponse;
import com.example.litelog.entity.User;
import com.example.litelog.repository.UserRepository;
import com.example.litelog.service.WeightRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/weight")
@RequiredArgsConstructor
public class WeightRecordController {

    private final WeightRecordService weightRecordService;
    private final UserRepository userRepository;

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
}