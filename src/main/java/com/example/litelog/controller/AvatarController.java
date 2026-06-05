package com.example.litelog.controller;

import com.example.litelog.dto.response.AvatarUploadResponse;
import com.example.litelog.service.AvatarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/user/avatar")
@RequiredArgsConstructor
public class AvatarController {

    private final AvatarService avatarService;

    @PostMapping("/upload")
    public ResponseEntity<AvatarUploadResponse> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            Authentication authentication,
            HttpServletRequest request) {
        
        String userId = authentication.getName();
        log.info("上传头像: userId={}", userId);
        
        // 检查是否为匿名用户
        if (userId == null || "anonymousUser".equals(userId)) {
            // 检查是否是 token 过期
            Boolean tokenExpired = (Boolean) request.getAttribute("tokenExpired");
            if (tokenExpired != null && tokenExpired) {
                AvatarUploadResponse response = AvatarUploadResponse.builder()
                        .success(false)
                        .message("登录已过期，请重新登录")
                        .build();
                return ResponseEntity.ok(response);
            }
            
            // 未登录
            AvatarUploadResponse response = AvatarUploadResponse.builder()
                    .success(false)
                    .message("请先登录")
                    .build();
            return ResponseEntity.ok(response);
        }
        
        AvatarUploadResponse response = avatarService.uploadAvatar(userId, file);
        return ResponseEntity.ok(response);
    }
}