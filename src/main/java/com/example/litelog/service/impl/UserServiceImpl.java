package com.example.litelog.service.impl;

import com.example.litelog.dto.request.UpdateProfileRequest;
import com.example.litelog.dto.response.FetchAllDataResponse;
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
import com.example.litelog.service.OssService;
import com.example.litelog.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import com.example.litelog.util.DateTimeUtils;
import com.example.litelog.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final WeightRecordRepository weightRecordRepository;
    private final UserIdentifierService userIdentifierService;
    private final OssService ossService;

    public UserServiceImpl(UserRepository userRepository, UserProfileRepository userProfileRepository, WeightRecordRepository weightRecordRepository, UserIdentifierService userIdentifierService, OssService ossService) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.weightRecordRepository = weightRecordRepository;
        this.userIdentifierService = userIdentifierService;
        this.ossService = ossService;
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

    @Override
    @Transactional
    public String uploadAvatar(String userId, String idType, byte[] imageData, String originalFilename) {
        try {
            User user = getUserByIdentifier(userId, idType);

            String extension = FileUtils.getExtensionFromFilename(originalFilename);
            String objectKey = "avatars/" + UUID.randomUUID().toString() + extension;
            String contentType = FileUtils.getContentTypeFromExtension(extension);

            // 删除旧头像
            ossService.deleteFile(user.getAvatarUrl());

            // 上传到 OSS
            String avatarUrl = ossService.uploadFile(objectKey, imageData, contentType);

            user.setAvatarUrl(avatarUrl);
            userRepository.save(user);

            log.info("头像上传成功：userId={}, idType={}, objectKey={}", userId, idType, objectKey);
            return avatarUrl;

        } catch (Exception e) {
            log.error("头像上传失败：userId={}, idType={}, error={}", userId, idType, e.getMessage());
            throw new BusinessException("头像上传失败");
        }
    }

    @Override
    public FetchAllDataResponse fetchAllData(String userId, String idType, Integer page, Integer size) {
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

        UpdateProfileResponse profileData = UpdateProfileResponse.builder()
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

        List<FetchAllDataResponse.RecordData> recordsData = records.stream().map(record ->
                new FetchAllDataResponse.RecordData(
                        record.getRecordId(),
                        record.getWeight(),
                        record.getBodyFatPercentage(),
                        record.getWaistCircumference(),
                        record.getHipCircumference(),
                        record.getChestCircumference(),
                        record.getThighCircumference(),
                        record.getNote(),
                        DateTimeUtils.toEpochSeconds(record.getDate()),
                        DateTimeUtils.toEpochSeconds(record.getCreatedAt()),
                        DateTimeUtils.toEpochSeconds(record.getUpdatedAt()),
                        record.getImageUrl(),
                        record.getMeasurementTimePeriod()
                )
        ).toList();

        FetchAllDataResponse response = FetchAllDataResponse.builder()
                .success(true)
                .message("获取成功")
                .profile(profileData)
                .records(recordsData)
                .totalRecords(totalRecords)
                .currentPage(currentPage)
                .totalPages(totalPages)
                .pageSize(pageSize)
                .build();

        log.info("获取用户所有数据成功：userId={}, idType={}, 记录数={}, 分页={}/{}",
                userId, idType, records.size(), currentPage, totalPages);

        return response;
    }

}