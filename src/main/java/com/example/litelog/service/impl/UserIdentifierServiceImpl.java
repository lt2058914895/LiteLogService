package com.example.litelog.service.impl;

import com.example.litelog.entity.User;
import com.example.litelog.repository.UserRepository;
import com.example.litelog.service.UserIdentifierService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserIdentifierServiceImpl implements UserIdentifierService {

    private static final Logger log = LoggerFactory.getLogger(UserIdentifierServiceImpl.class);

    private final UserRepository userRepository;

    public UserIdentifierServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final int MAX_CREATE_RETRY = 3;

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

        // 使用数据库唯一约束 + 重试替代 while 循环，避免高并发下潜在死循环
        for (int i = 0; i < MAX_CREATE_RETRY; i++) {
            try {
                User newUser = User.builder()
                        .phone(generatePhone())
                        .password("")
                        .nickname("用户")
                        .build();

                switch (idType) {
                    case "device":
                        newUser.setDeviceId(userId);
                        break;
                    case "custom_phone":
                        newUser.setCustomPhone(userId);
                        break;
                    case "custom_email":
                        newUser.setCustomEmail(userId);
                        break;
                    default:
                        newUser.setDeviceId(userId);
                }

                User savedUser = userRepository.saveAndFlush(newUser);
                log.info("创建新用户：id={}, type={}, userId={}", userId, idType, savedUser.getId());
                return savedUser.getId();
            } catch (DataIntegrityViolationException e) {
                log.warn("创建用户时phone冲突，重试({}/{}): userId={}, idType={}", i + 1, MAX_CREATE_RETRY, userId, idType);
            }
        }

        // 极端情况：多次重试仍冲突，用完整UUID确保唯一
        User fallbackUser = User.builder()
                .phone("user_" + UUID.randomUUID().toString())
                .password("")
                .nickname("用户")
                .build();
        switch (idType) {
            case "device": fallbackUser.setDeviceId(userId); break;
            case "custom_phone": fallbackUser.setCustomPhone(userId); break;
            case "custom_email": fallbackUser.setCustomEmail(userId); break;
            default: fallbackUser.setDeviceId(userId);
        }
        User savedUser = userRepository.saveAndFlush(fallbackUser);
        log.info("创建新用户(fallback)：id={}, type={}, userId={}", userId, idType, savedUser.getId());
        return savedUser.getId();
    }

    private Optional<User> findUserByIdentifier(String userId, String idType) {
        switch (idType) {
            case "device":
                return userRepository.findByDeviceId(userId);
            case "custom_phone":
                return userRepository.findByCustomPhone(userId);
            case "custom_email":
                return userRepository.findByCustomEmail(userId);
            default:
                return userRepository.findByDeviceId(userId);
        }
    }

    private String generatePhone() {
        return "user_" + UUID.randomUUID().toString().substring(0, 10);
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
}