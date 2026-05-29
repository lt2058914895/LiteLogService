package com.example.litelog.service;

import com.example.litelog.dto.request.LoginRequest;
import com.example.litelog.dto.request.RegisterRequest;
import com.example.litelog.dto.request.SmsLoginRequest;
import com.example.litelog.dto.response.LoginResponse;
import com.example.litelog.dto.response.LogoutResponse;
import com.example.litelog.dto.response.RegisterResponse;
import com.example.litelog.entity.User;
import com.example.litelog.repository.UserRepository;
import com.example.litelog.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final SmsService smsService;
    private final TokenBlacklistService tokenBlacklistService;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (!smsService.verifyCode(request.getPhone(), request.getCode())) {
            return RegisterResponse.builder()
                    .success(false)
                    .message("验证码错误或已过期")
                    .build();
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            return RegisterResponse.builder()
                    .success(false)
                    .message("该手机号已注册，请直接去登录")
                    .build();
        }

        User user = User.builder()
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(generateRandomNickname())
                .build();

        User savedUser = userRepository.save(user);
        log.info("用户注册成功: {}, 昵称: {}", request.getPhone(), savedUser.getNickname());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getPhone());
        String token = jwtUtil.generateToken(userDetails);

        return RegisterResponse.builder()
                .success(true)
                .userId(String.valueOf(savedUser.getId()))
                .nickname(savedUser.getNickname())
                .avatarUrl(savedUser.getAvatarUrl())
                .token(token)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getExpiration())
                .message("注册成功")
                .build();
    }

    private String generateRandomNickname() {
        String[] adjectives = {"Ace", "Brave", "Champ", "Dash", "Echo", "Flash", "Glow", "Hero", 
                               "Iggy", "Jazz", "Kai", "Luna", "Max", "Nova", "Onyx", "Pulse", 
                               "Quest", "Rush", "Sky", "Tiger", "Ultra", "Vibe", "Wave", "Xen", "Yolo", "Zest"};
        String adjective = adjectives[(int) (Math.random() * adjectives.length)];
        int suffix = (int) (Math.random() * 9000) + 1000;
        return adjective + suffix;
    }

    public LoginResponse loginWithPassword(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getPhone(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getPhone());
            User user = userRepository.findByPhone(request.getPhone()).orElse(null);

            if (user == null) {
                return LoginResponse.builder()
                        .success(false)
                        .message("用户不存在")
                        .build();
            }

            String token = jwtUtil.generateToken(userDetails);

            return LoginResponse.builder()
                    .success(true)
                    .userId(String.valueOf(user.getId()))
                    .nickname(user.getNickname())
                    .avatarUrl(user.getAvatarUrl())
                    .token(token)
                    .tokenType("Bearer")
                    .expiresIn(jwtUtil.getExpiration())
                    .message("登录成功")
                    .build();
        } catch (Exception e) {
            log.warn("密码登录失败: {}, 错误: {}", request.getPhone(), e.getMessage());
            return LoginResponse.builder()
                    .success(false)
                    .message("用户名或密码错误")
                    .build();
        }
    }

    public LoginResponse loginWithSMSCode(SmsLoginRequest request) {
        if (!smsService.verifyCode(request.getPhone(), request.getCode())) {
            return LoginResponse.builder()
                    .success(false)
                    .message("验证码错误或已过期")
                    .build();
        }

        User user = userRepository.findByPhone(request.getPhone()).orElse(null);
        if (user == null) {
            return LoginResponse.builder()
                    .success(false)
                    .message("用户不存在")
                    .build();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getPhone());
        String token = jwtUtil.generateToken(userDetails);

        log.info("短信登录成功: {}", request.getPhone());

        return LoginResponse.builder()
                .success(true)
                .userId(String.valueOf(user.getId()))
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .token(token)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getExpiration())
                .message("登录成功")
                .build();
    }

    public LogoutResponse logout(String token) {
        try {
            String cleanToken = token.replace("Bearer ", "").trim();
            
            long expirationTime = jwtUtil.getExpiration() * 1000 + System.currentTimeMillis();
            tokenBlacklistService.blacklistToken(cleanToken, expirationTime);
            
            String username = jwtUtil.extractUsername(cleanToken);
            log.info("用户退出登录: {}", username);
            
            return LogoutResponse.builder()
                    .success(true)
                    .message("退出登录成功")
                    .build();
        } catch (Exception e) {
            log.warn("退出登录失败: {}", e.getMessage());
            return LogoutResponse.builder()
                    .success(true)
                    .message("退出登录成功")
                    .build();
        }
    }
}