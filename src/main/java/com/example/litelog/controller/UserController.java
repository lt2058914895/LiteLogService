package com.example.litelog.controller;

import com.example.litelog.dto.request.UpdateProfileRequest;
import com.example.litelog.dto.response.GetProfileResponse;
import com.example.litelog.dto.response.UpdateProfileResponse;
import com.example.litelog.service.UserService;
import com.example.litelog.util.FileUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<GetProfileResponse> getProfile(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-Id-Type", required = false, defaultValue = "device") String idType) {
        
        log.info("获取用户资料: userId={}, idType={}", userId, idType);
        
        GetProfileResponse response = userService.getProfile(userId, idType);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<UpdateProfileResponse> updateProfile(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-Id-Type", required = false, defaultValue = "device") String idType,
            @Valid @RequestBody UpdateProfileRequest request) {
        
        log.info("更新用户信息: userId={}, idType={}, nickname={}", userId, idType, request.getNickname());
        
        UpdateProfileResponse response = userService.updateProfile(userId, idType, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/avatar/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadAvatar(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-Id-Type", required = false, defaultValue = "device") String idType,
            @RequestParam("file") MultipartFile file) {
        
        log.info("头像上传: userId={}, idType={}, 文件名={}, 大小={}", userId, idType, file.getOriginalFilename(), file.getSize());
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "请选择图片");
                return ResponseEntity.badRequest().body(response);
            }
            
            String filename = file.getOriginalFilename();
            String extension = FileUtils.getExtensionFromFilename(filename);
            if (!FileUtils.isValidImageExtension(extension)) {
                response.put("success", false);
                response.put("message", "仅支持 jpg/jpeg/png/gif 格式");
                return ResponseEntity.badRequest().body(response);
            }
            
            String avatarUrl = userService.uploadAvatar(userId, idType, file.getBytes(), filename);
            
            response.put("success", true);
            response.put("message", "上传成功");
            response.put("avatarUrl", avatarUrl);
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            log.error("头像上传失败：userId={}, idType={}, error={}", userId, idType, e.getMessage());
            response.put("success", false);
            response.put("message", "上传失败，请重试");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/id")
    public ResponseEntity<Map<String, Object>> getUserId(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-Id-Type", required = false, defaultValue = "device") String idType) {
        
        log.info("获取用户ID: userId={}, idType={}", userId, idType);
        
        Map<String, Object> response = new HashMap<>();
        try {
            Long userIdValue = userService.getOrCreateUserId(userId, idType);
            response.put("success", true);
            response.put("userId", userIdValue);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取用户ID失败：userId={}, idType={}, error={}", userId, idType, e.getMessage());
            response.put("success", false);
            response.put("message", "获取用户ID失败");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/fetch-all-data")
    public ResponseEntity<Object> fetchAllData(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-Id-Type", required = false, defaultValue = "device") String idType) {
        
        log.info("获取用户所有数据: userId={}, idType={}", userId, idType);
        
        Object response = userService.fetchAllData(userId, idType);
        return ResponseEntity.ok(response);
    }

}