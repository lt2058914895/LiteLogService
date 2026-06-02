package com.example.litelog.service.impl;

import com.example.litelog.dto.request.UpdateProfileRequest;
import com.example.litelog.dto.response.UpdateProfileResponse;
import com.example.litelog.entity.User;
import com.example.litelog.repository.UserRepository;
import com.example.litelog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UpdateProfileResponse updateProfile(String userId, UpdateProfileRequest request) {
        try {
            // userId 实际上是手机号
            Optional<User> optionalUser = userRepository.findByPhone(userId);
            
            if (optionalUser.isEmpty()) {
                log.warn("用户不存在：{}", userId);
                return UpdateProfileResponse.builder()
                        .success(false)
                        .message("用户不存在")
                        .build();
            }

            User user = optionalUser.get();
            user.setNickname(request.getNickname());
            
            if (request.getAvatarUrl() != null) {
                user.setAvatarUrl(request.getAvatarUrl());
            }

            User savedUser = userRepository.save(user);
            log.info("用户信息更新成功：{}, 昵称：{}", userId, savedUser.getNickname());

            return UpdateProfileResponse.builder()
                    .success(true)
                    .message("更新成功")
                    .nickname(savedUser.getNickname())
                    .avatarUrl(savedUser.getAvatarUrl())
                    .build();

        } catch (Exception e) {
            log.error("更新用户信息失败：{}", e.getMessage());
            return UpdateProfileResponse.builder()
                    .success(false)
                    .message("更新失败，请重试")
                    .build();
        }
    }
}