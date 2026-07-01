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
import com.example.litelog.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public UserServiceImpl(UserRepository userRepository, UserProfileRepository userProfileRepository, WeightRecordRepository weightRecordRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.weightRecordRepository = weightRecordRepository;
    }

    @Override
    @Transactional
    public Long getOrCreateUserId(String userId, String idType) {
        if (userId == null || userId.isEmpty()) {
            log.warn("用户ID为空，使用默认用户");
            return getOrCreateDefaultUser().getId();
        }

        Optional<User> existingUser = findUserByIdentifier(userId, idType);
        if (existingUser.isPresent()) {
            log.info("找到已有用户：id={}, type={}, userId={}", userId, idType, existingUser.get().getId());
            return existingUser.get().getId();
        }

        User newUser = User.builder()
                .phone(generateUniquePhone())
                .password("")
                .nickname("用户")
                .build();

        if ("device".equals(idType)) {
            newUser.setDeviceId(userId);
        }

        User savedUser = userRepository.save(newUser);
        
        getOrCreateUserProfile(savedUser.getId());
        
        log.info("创建新用户：id={}, type={}, userId={}", userId, idType, savedUser.getId());
        return savedUser.getId();
    }

    private Optional<User> findUserByIdentifier(String userId, String idType) {
        if ("device".equals(idType)) {
            return userRepository.findByDeviceId(userId);
        }
        return userRepository.findByDeviceId(userId);
    }

    private User getUserByIdentifier(String userId, String idType) {
        if (userId == null || userId.isEmpty()) {
            return getOrCreateDefaultUser();
        }

        Optional<User> existingUser = findUserByIdentifier(userId, idType);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        return getOrCreateDefaultUser();
    }

    private String generateUniquePhone() {
        String phone;
        do {
            phone = "user_" + UUID.randomUUID().toString().substring(0, 10);
        } while (userRepository.existsByPhone(phone));
        return phone;
    }

    private User getOrCreateDefaultUser() {
        Optional<User> existingUser = userRepository.findById(1L);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        User newUser = User.builder()
                .id(1L)
                .phone("default_user")
                .password("")
                .nickname("用户")
                .build();
        return userRepository.save(newUser);
    }

    private UserProfile getOrCreateUserProfile(Long userId) {
        Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(userId);
        if (existingProfile.isPresent()) {
            return existingProfile.get();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

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
        try {
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

        } catch (Exception e) {
            log.error("更新用户资料失败：userId={}, idType={}, error={}", userId, idType, e.getMessage());
            return UpdateProfileResponse.builder()
                    .success(false)
                    .message("更新失败，请重试")
                    .build();
        }
    }

    @Override
    public GetProfileResponse getProfile(String userId, String idType) {
        try {
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

        } catch (Exception e) {
            log.error("获取用户资料失败：userId={}, idType={}, error={}", userId, idType, e.getMessage());
            return GetProfileResponse.builder()
                    .success(false)
                    .message("获取失败，请重试")
                    .build();
        }
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

            String newFilename = originalFilename;
            Path newFilePath = storageDir.resolve(newFilename);

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

    @Override
    public Object fetchAllData(String userId, String idType) {
        try {
            User user = getUserByIdentifier(userId, idType);
            UserProfile profile = getOrCreateUserProfile(user.getId());
            List<WeightRecord> records = weightRecordRepository.findByUserId(user.getId());

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
                recordMap.put("recordId", record.getId());
                recordMap.put("weight", record.getWeight());
                recordMap.put("bodyFatPercentage", record.getBodyFatPercentage());
                recordMap.put("waistCircumference", record.getWaistCircumference());
                recordMap.put("hipCircumference", record.getHipCircumference());
                recordMap.put("chestCircumference", record.getChestCircumference());
                recordMap.put("thighCircumference", record.getThighCircumference());
                recordMap.put("note", record.getNote());
                recordMap.put("date", record.getDate());
                recordMap.put("createdAt", record.getCreatedAt());
                recordMap.put("updatedAt", record.getUpdatedAt());
                recordMap.put("imageUrl", record.getImageUrl());
                recordMap.put("measurementTimePeriod", record.getMeasurementTimePeriod());
                return recordMap;
            }).toList();
            result.put("records", recordsData);

            log.info("获取用户所有数据成功：userId={}, idType={}, 记录数={}", userId, idType, records.size());
            
            return result;

        } catch (Exception e) {
            log.error("获取用户所有数据失败：userId={}, idType={}, error={}", userId, idType, e.getMessage());
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "获取失败，请重试");
            return errorResult;
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".jpg";
        }
        String extension = filename.substring(filename.lastIndexOf("."));
        String lowerExtension = extension.toLowerCase();
        if (lowerExtension.equals(".jpg") || lowerExtension.equals(".jpeg") ||
            lowerExtension.equals(".png") || lowerExtension.equals(".gif")) {
            return lowerExtension;
        }
        return ".jpg";
    }

    private void deleteOldAvatar(String oldAvatarUrl) {
        try {
            String filename = oldAvatarUrl.substring(AVATAR_URL_PREFIX.length());
            Path oldFilePath = Paths.get(AVATAR_STORAGE_DIR).resolve(filename);
            if (Files.exists(oldFilePath)) {
                Files.delete(oldFilePath);
                log.info("旧头像已删除：{}", filename);
            }
        } catch (IOException e) {
            log.warn("删除旧头像失败：{}", e.getMessage());
        }
    }
}