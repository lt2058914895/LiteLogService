package com.example.litelog.controller;

import com.example.litelog.dto.request.LoginRequest;
import com.example.litelog.dto.request.RegisterRequest;
import com.example.litelog.dto.request.ResetPasswordRequest;
import com.example.litelog.dto.request.SmsLoginRequest;
import com.example.litelog.dto.request.SmsSendRequest;
import com.example.litelog.dto.response.LoginResponse;
import com.example.litelog.dto.response.LogoutResponse;
import com.example.litelog.dto.response.RegisterResponse;
import com.example.litelog.dto.response.ResetPasswordResponse;
import com.example.litelog.dto.response.SmsResponse;
import com.example.litelog.service.AuthService;
import com.example.litelog.service.SmsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SmsService smsService;

    @PostMapping("/sms/send")
    public ResponseEntity<SmsResponse> sendSMSCode(@Valid @RequestBody SmsSendRequest request) {
        boolean success = smsService.sendSMSCode(request.getPhone(), request.getType());
        if (success) {
            return ResponseEntity.ok(SmsResponse.builder()
                    .success(true)
                    .message("验证码发送成功")
                    .build());
        } else {
            return ResponseEntity.ok(SmsResponse.builder()
                    .success(false)
                    .message("发送过于频繁，请稍后重试")
                    .build());
        }
    }

    @PostMapping("/login/password")
    public ResponseEntity<LoginResponse> loginWithPassword(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.loginWithPassword(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/sms")
    public ResponseEntity<LoginResponse> loginWithSMSCode(@Valid @RequestBody SmsLoginRequest request) {
        LoginResponse response = authService.loginWithSMSCode(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/password/reset")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        ResetPasswordResponse response = authService.resetPassword(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        LogoutResponse response = authService.logout(token != null ? token : "");
        return ResponseEntity.ok(response);
    }
}