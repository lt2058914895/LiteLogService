package com.example.litelog.controller;

import com.example.litelog.dto.request.UpdateProfileRequest;
import com.example.litelog.dto.response.GetProfileResponse;
import com.example.litelog.dto.response.UpdateProfileResponse;
import com.example.litelog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<GetProfileResponse> getProfile() {
        log.info("获取用户资料");
        
        GetProfileResponse response = userService.getProfile();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<UpdateProfileResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {
        
        log.info("更新用户信息: nickname={}", request.getNickname());
        
        UpdateProfileResponse response = userService.updateProfile(request);
        return ResponseEntity.ok(response);
    }
}