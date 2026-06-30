package com.example.litelog.service.impl;

import com.example.litelog.dto.request.UpdateProfileRequest;
import com.example.litelog.dto.response.GetProfileResponse;
import com.example.litelog.dto.response.UpdateProfileResponse;
import com.example.litelog.entity.User;
import com.example.litelog.entity.UserProfile;
import com.example.litelog.repository.UserProfileRepository;
import com.example.litelog.repository.UserRepository;
import com.example.litelog.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    private static final Long DEFAULT_USER_ID = 1L;

    private User getOrCreateDefaultUser() {
        Optional<User> existingUser = userRepository.findById(DEFAULT_USER_ID);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        User newUser = User.builder()
                .id(DEFAULT_USER_ID)
                .phone("default_user")
                .password("")
                .nickname("用户")
                .build();
        return userRepository.save(newUser);
    }

    private UserProfile getOrCreateUserProfile() {
        User user = getOrCreateDefaultUser();
        
        Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(user.getId());
        if (existingProfile.isPresent()) {
            return existingProfile.get();
        }

        UserProfile newProfile = UserProfile.builder()
                .user(user)
                .height(170.0)
                .gender(0)
                .age(30)
                .goalWeight(65.0)
                .goalBodyFat(null)
                .goalWaistCircumference(null)
                .goalHipCircumference(null)
                .goalChestCircumference(null)
                .goalThighCircumference(null)
                .weightUnit("kg")
                .build();
        return userProfileRepository.save(newProfile);
    }

    @Override
    @Transactional
    public UpdateProfileResponse updateProfile(UpdateProfileRequest request) {
        try {
            User user = getOrCreateDefaultUser();
            UserProfile profile = getOrCreateUserProfile();

            if (request.getNickname() != null) {
                user.setNickname(request.getNickname());
            }

            if (request.getAvatarUrl() != null) {
                user.setAvatarUrl(request.getAvatarUrl());
            }

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

            if (request.getGoalBodyFat() != null) {
                profile.setGoalBodyFat(request.getGoalBodyFat());
            }

            if (request.getGoalWaistCircumference() != null) {
                profile.setGoalWaistCircumference(request.getGoalWaistCircumference());
            }

            if (request.getGoalHipCircumference() != null) {
                profile.setGoalHipCircumference(request.getGoalHipCircumference());
            }

            if (request.getGoalChestCircumference() != null) {
                profile.setGoalChestCircumference(request.getGoalChestCircumference());
            }

            if (request.getGoalThighCircumference() != null) {
                profile.setGoalThighCircumference(request.getGoalThighCircumference());
            }

            if (request.getWeightUnit() != null) {
                profile.setWeightUnit(request.getWeightUnit());
            }

            userRepository.save(user);
            userProfileRepository.save(profile);

            log.info("用户资料更新成功：昵称：{}", user.getNickname());

            return UpdateProfileResponse.builder()
                    .success(true)
                    .message("更新成功")
                    .nickname(user.getNickname())
                    .avatarUrl(user.getAvatarUrl())
                    .height(profile.getHeight())
                    .gender(profile.getGender())
                    .age(profile.getAge())
                    .goalWeight(profile.getGoalWeight())
                    .goalBodyFat(profile.getGoalBodyFat())
                    .goalWaistCircumference(profile.getGoalWaistCircumference())
                    .goalHipCircumference(profile.getGoalHipCircumference())
                    .goalChestCircumference(profile.getGoalChestCircumference())
                    .goalThighCircumference(profile.getGoalThighCircumference())
                    .weightUnit(profile.getWeightUnit())
                    .build();

        } catch (Exception e) {
            log.error("更新用户资料失败：{}", e.getMessage());
            return UpdateProfileResponse.builder()
                    .success(false)
                    .message("更新失败，请重试")
                    .build();
        }
    }

    @Override
    public GetProfileResponse getProfile() {
        try {
            User user = getOrCreateDefaultUser();
            UserProfile profile = getOrCreateUserProfile();

            return GetProfileResponse.builder()
                    .success(true)
                    .message("获取成功")
                    .nickname(user.getNickname())
                    .avatarUrl(user.getAvatarUrl())
                    .height(profile.getHeight())
                    .gender(profile.getGender())
                    .age(profile.getAge())
                    .goalWeight(profile.getGoalWeight())
                    .goalBodyFat(profile.getGoalBodyFat())
                    .goalWaistCircumference(profile.getGoalWaistCircumference())
                    .goalHipCircumference(profile.getGoalHipCircumference())
                    .goalChestCircumference(profile.getGoalChestCircumference())
                    .goalThighCircumference(profile.getGoalThighCircumference())
                    .weightUnit(profile.getWeightUnit())
                    .build();

        } catch (Exception e) {
            log.error("获取用户资料失败：{}", e.getMessage());
            return GetProfileResponse.builder()
                    .success(false)
                    .message("获取失败，请重试")
                    .build();
        }
    }
}