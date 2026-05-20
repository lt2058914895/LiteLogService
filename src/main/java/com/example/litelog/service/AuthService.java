package com.example.litelog.service;

import com.example.litelog.dto.request.LoginRequest;
import com.example.litelog.dto.request.RegisterRequest;
import com.example.litelog.dto.request.SmsLoginRequest;
import com.example.litelog.dto.response.LoginResponse;
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
                    .message("该手机号已注册")
                    .build();
        }

        User user = User.builder()
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);
        log.info("用户注册成功: {}", request.getPhone());

        return RegisterResponse.builder()
                .success(true)
                .userId(savedUser.getId())
                .message("注册成功")
                .build();
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
                    .userId(user.getId())
                    .accessToken(token)
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
                .userId(user.getId())
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getExpiration())
                .message("登录成功")
                .build();
    }
}