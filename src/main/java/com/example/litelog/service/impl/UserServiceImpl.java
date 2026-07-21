package com.example.litelog.service.impl;

import com.example.litelog.dto.request.UpdateProfileRequest;
import com.example.litelog.dto.response.GetProfileResponse;
import com.example.litelog.dto.response.UpdateProfileResponse;
import com.example.litelog.entity.User;
import com.example.litelog.entity.UserProfile;
import com.example.litelog.entity.WeightRecord;
import com.example.litelog.exception.BusinessException;
import com.example.litelog.repository.UserProfileRepository;
import com.example.litelog.repository.UserRepository;
import com.example.litelog.repository.WeightRecordRepository;
import com.example.litelog.service.UserIdentifierService;
import com.example.litelog.service.UserService;
import jakarta.transaction.Transactional;
import com.example.litelog.util.DateTimeUtils;
import com.example.litelog.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final WeightRecordRepository weightRecordRepository;
    private final UserIdentifierService userIdentifierService;

    public UserServiceImpl(UserRepository userRepository, UserProfileRepository userProfileRepository, WeightRecordRepository weightRecordRepository, UserIdentifierService userIdentifierService) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.weightRecordRepository = weightRecordRepository;
        this.userIdentifierService = userIdentifierService;
    }

    @Override
    @Transactional
    public Long getOrCreateUserId(String userId, String idType) {
        Long userIdValue = userIdentifierService.getOrCreateUserId(userId, idType);
        getOrCreateUserProfile(userIdValue);
        return userIdValue;
    }

    private User getUserByIdentifier(String userId, String idType) {
        Long userIdValue = userIdentifierService.getOrCreateUserId(userId, idType);
        return userRepository.findById(userIdValue)
                .orElseThrow(() -> new BusinessException("用户不存在"));
    }

    private UserProfile getOrCreateUserProfile(Long userId) {
        Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(userId);
        if (existingProfile.isPresent()) {
            return existingProfile.get();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));

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
    public UpdateProfileResponse updateProfile(String userId, String idType, UpdateProfileRequest request) {
        User user = getUserByIdentifier(userId, idType);
        UserProfile profile = getOrCreateUserProfile(user.getId());

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

        log.info("用户资料更新成功：userId={}, idType={}, 昵称：{}", userId, idType, user.getNickname());

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
    }

    @Override
    public GetProfileResponse getProfile(String userId, String idType) {
        User user = getUserByIdentifier(userId, idType);
        UserProfile profile = getOrCreateUserProfile(user.getId());

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
    }

    private static final String AVATAR_STORAGE_DIR = "./uploads/avatars";
    private static final String AVATAR_URL_PREFIX = "/uploads/avatars/";

    @Override
    @Transactional
    public String uploadAvatar(String userId, String idType, byte[] imageData, String originalFilename) {
        try {
            User user = getUserByIdentifier(userId, idType);

            Path storageDir = Paths.get(AVATAR_STORAGE_DIR);
            if (!Files.exists(storageDir)) {
                Files.createDirectories(storageDir);
            }

            // 使用UUID命名避免文件名冲突
            String extension = FileUtils.getExtensionFromFilename(originalFilename);
            String newFilename = UUID.randomUUID().toString() + extension;
            Path newFilePath = storageDir.resolve(newFilename);

            // 删除旧头像文件
            deleteOldAvatar(user.getAvatarUrl());

            Files.write(newFilePath, imageData);

            String avatarUrl = AVATAR_URL_PREFIX + newFilename;
            user.setAvatarUrl(avatarUrl);
            userRepository.save(user);

            log.info("头像上传成功：userId={}, idType={}, 文件={}", userId, idType, newFilename);
            return avatarUrl;

        } catch (IOException e) {
            log.error("头像上传失败：userId={}, idType={}, error={}", userId, idType, e.getMessage());
            throw new BusinessException("头像上传失败");
        }
    }

    private void deleteOldAvatar(String avatarUrl) {
        if (avatarUrl == null || avatarUrl.isEmpty()) {
            return;
        }
        try {
            String fileName = avatarUrl.substring(avatarUrl.lastIndexOf("/") + 1);
            Path filePath = Paths.get(AVATAR_STORAGE_DIR).resolve(fileName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("旧头像已删除：{}", filePath);
            }
        } catch (Exception e) {
            log.warn("删除旧头像失败：{}", e.getMessage());
        }
    }

    @Override
    public Object fetchAllData(String userId, String idType, Integer page, Integer size) {
        User user = getUserByIdentifier(userId, idType);
        UserProfile profile = getOrCreateUserProfile(user.getId());

        // 分页查询体重记录，默认无分页（兼容旧客户端），最大上限1000条
        List<WeightRecord> records;
        long totalRecords = 0;
        int currentPage = 0;
        int totalPages = 1;
        int pageSize = 1000; // 无分页时的默认上限

        if (page != null && size != null && page >= 0 && size > 0) {
            // 客户端指定分页参数
            pageSize = Math.min(size, 100); // 单页最大100条
            Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "date"));
            Page<WeightRecord> recordPage = weightRecordRepository.findByUserId(user.getId(), pageable);
            records = recordPage.getContent();
            totalRecords = recordPage.getTotalElements();
            currentPage = recordPage.getNumber();
            totalPages = recordPage.getTotalPages();
        } else {
            // 无分页，返回全部记录（有上限保护）
            records = weightRecordRepository.findByUserIdOrderByDateDesc(user.getId());
            totalRecords = records.size();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "获取成功");

        Map<String, Object> profileData = new HashMap<>();
        profileData.put("nickname", user.getNickname());
        profileData.put("avatarUrl", user.getAvatarUrl());
        profileData.put("height", profile.getHeight());
        profileData.put("gender", profile.getGender());
        profileData.put("age", profile.getAge());
        profileData.put("goalWeight", profile.getGoalWeight());
        profileData.put("goalBodyFat", profile.getGoalBodyFat());
        profileData.put("goalWaistCircumference", profile.getGoalWaistCircumference());
        profileData.put("goalHipCircumference", profile.getGoalHipCircumference());
        profileData.put("goalChestCircumference", profile.getGoalChestCircumference());
        profileData.put("goalThighCircumference", profile.getGoalThighCircumference());
        profileData.put("weightUnit", profile.getWeightUnit());
        result.put("profile", profileData);

        List<Map<String, Object>> recordsData = records.stream().map(record -> {
            Map<String, Object> recordMap = new HashMap<>();
            recordMap.put("recordId", record.getRecordId());
            recordMap.put("weight", record.getWeight());
            recordMap.put("bodyFatPercentage", record.getBodyFatPercentage());
            recordMap.put("waistCircumference", record.getWaistCircumference());
            recordMap.put("hipCircumference", record.getHipCircumference());
            recordMap.put("chestCircumference", record.getChestCircumference());
            recordMap.put("thighCircumference", record.getThighCircumference());
            recordMap.put("note", record.getNote());
            recordMap.put("date", DateTimeUtils.toEpochSeconds(record.getDate()));
            recordMap.put("createdAt", DateTimeUtils.toEpochSeconds(record.getCreatedAt()));
            recordMap.put("updatedAt", DateTimeUtils.toEpochSeconds(record.getUpdatedAt()));
            recordMap.put("imageUrl", record.getImageUrl());
            recordMap.put("measurementTimePeriod", record.getMeasurementTimePeriod());
            return recordMap;
        }).toList();
        result.put("records", recordsData);

        // 分页元数据
        result.put("totalRecords", totalRecords);
        result.put("currentPage", currentPage);
        result.put("totalPages", totalPages);
        result.put("pageSize", pageSize);

        log.info("获取用户所有数据成功：userId={}, idType={}, 记录数={}, 分页={}/{}", 
                userId, idType, records.size(), currentPage, totalPages);
        
        return result;
    }

}