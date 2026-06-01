package com.example.litelog.controller;

import com.example.litelog.dto.request.UpdateProfileRequest;
import com.example.litelog.dto.response.UpdateProfileResponse;
import com.example.litelog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/profile")
    public ResponseEntity<UpdateProfileResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        
        String userId = authentication.getName();
        log.info("更新用户信息: userId={}, nickname={}", userId, request.getNickname());
        
        UpdateProfileResponse response = userService.updateProfile(userId, request);
        return ResponseEntity.ok(response);
    }
}