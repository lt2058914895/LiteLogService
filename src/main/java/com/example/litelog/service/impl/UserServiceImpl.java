package com.example.litelog.service.impl;

import com.example.litelog.dto.request.UpdateProfileRequest;
import com.example.litelog.dto.response.UpdateProfileResponse;
import com.example.litelog.entity.User;
import com.example.litelog.entity.UserProfile;
import com.example.litelog.repository.UserProfileRepository;
import com.example.litelog.repository.UserRepository;
import com.example.litelog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    @Transactional
    public UpdateProfileResponse updateProfile(String userId, UpdateProfileRequest request) {
        try {
            Long userIdLong = Long.parseLong(userId);
            User user = userRepository.findById(userIdLong).orElse(null);
            
            if (user == null) {
                log.warn("用户不存在：{}", userId);
                return UpdateProfileResponse.builder()
                        .success(false)
                        .message("用户不存在")
                        .build();
            }

            // 更新 User 表中的 nickname 和 avatar_url
            if (request.getNickname() != null) {
                user.setNickname(request.getNickname());
            }
            
            if (request.getAvatarUrl() != null) {
                user.setAvatarUrl(request.getAvatarUrl());
            }
            
            User savedUser = userRepository.save(user);
            
            // 更新 UserProfile 表中的其他字段
            UserProfile profile = userProfileRepository.findByUserId(userIdLong)
                    .orElse(UserProfile.builder()
                            .user(user)
                            .height(170.0)
                            .gender(0)
                            .age(30)
                            .goalWeight(65.0)
                            .build());

            if (request.getHeight() != null) {
                profile.setHeight(request.getHeight());
            }
            
            if (request.getGender() != null) {
                profile.setGender(request.getGender());
            }
            
            if (request.getAge() != null) {
                profile.setAge(request.getAge());
            }
            
            if (request.getGoalWeight() != null) {
                profile.setGoalWeight(request.getGoalWeight());
            }

            userProfileRepository.save(profile);
            log.info("用户资料更新成功：{}, 昵称：{}", userId, savedUser.getNickname());

            return UpdateProfileResponse.builder()
                    .success(true)
                    .message("更新成功")
                    .nickname(savedUser.getNickname())
                    .avatarUrl(savedUser.getAvatarUrl())
                    .build();

        } catch (NumberFormatException e) {
            log.warn("无效的用户ID格式：{}", userId);
            return UpdateProfileResponse.builder()
                    .success(false)
                    .message("无效的用户ID")
                    .build();
        } catch (Exception e) {
            log.error("更新用户资料失败：{}", e.getMessage());
            return UpdateProfileResponse.builder()
                    .success(false)
                    .message("更新失败，请重试")
                    .build();
        }
    }
}