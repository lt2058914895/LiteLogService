package com.example.litelog.service.impl;

import com.example.litelog.dto.request.UpdateProfileRequest;
import com.example.litelog.dto.response.GetProfileResponse;
import com.example.litelog.dto.response.UpdateProfileResponse;
import com.example.litelog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    // 简单的内存存储，实际应用中可以使用数据库
    private String nickname = "用户";
    private String avatarUrl = null;
    private Double height = 170.0;
    private Integer gender = 0;
    private Integer age = 30;
    private Double goalWeight = 65.0;
    private Double goalBodyFat = null;
    private Double goalWaistCircumference = null;
    private Double goalHipCircumference = null;
    private Double goalChestCircumference = null;
    private Double goalThighCircumference = null;
    private String weightUnit = "kg";

    @Override
    public UpdateProfileResponse updateProfile(UpdateProfileRequest request) {
        try {
            if (request.getNickname() != null) {
                this.nickname = request.getNickname();
            }
            
            if (request.getAvatarUrl() != null) {
                this.avatarUrl = request.getAvatarUrl();
            }
            
            if (request.getHeight() != null) {
                this.height = request.getHeight();
            }
            
            if (request.getGender() != null) {
                this.gender = request.getGender();
            }
            
            if (request.getAge() != null) {
                this.age = request.getAge();
            }
            
            if (request.getGoalWeight() != null) {
                this.goalWeight = request.getGoalWeight();
            }
            
            if (request.getGoalBodyFat() != null) {
                this.goalBodyFat = request.getGoalBodyFat();
            }
            
            if (request.getGoalWaistCircumference() != null) {
                this.goalWaistCircumference = request.getGoalWaistCircumference();
            }
            
            if (request.getGoalHipCircumference() != null) {
                this.goalHipCircumference = request.getGoalHipCircumference();
            }
            
            if (request.getGoalChestCircumference() != null) {
                this.goalChestCircumference = request.getGoalChestCircumference();
            }
            
            if (request.getGoalThighCircumference() != null) {
                this.goalThighCircumference = request.getGoalThighCircumference();
            }
            
            if (request.getWeightUnit() != null) {
                this.weightUnit = request.getWeightUnit();
            }

            log.info("用户资料更新成功：昵称：{}", this.nickname);

            return UpdateProfileResponse.builder()
                    .success(true)
                    .message("更新成功")
                    .nickname(this.nickname)
                    .avatarUrl(this.avatarUrl)
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
            return GetProfileResponse.builder()
                    .success(true)
                    .message("获取成功")
                    .nickname(this.nickname)
                    .avatarUrl(this.avatarUrl)
                    .height(this.height)
                    .gender(this.gender)
                    .age(this.age)
                    .goalWeight(this.goalWeight)
                    .goalBodyFat(this.goalBodyFat)
                    .goalWaistCircumference(this.goalWaistCircumference)
                    .goalHipCircumference(this.goalHipCircumference)
                    .goalChestCircumference(this.goalChestCircumference)
                    .goalThighCircumference(this.goalThighCircumference)
                    .weightUnit(this.weightUnit)
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