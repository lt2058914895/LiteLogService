package com.example.litelog.service.impl;

import com.example.litelog.entity.User;
import com.example.litelog.repository.UserRepository;
import com.example.litelog.service.UserIdentifierService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        User savedUser = userRepository.save(newUser);
        log.info("创建新用户：id={}, type={}, userId={}", userId, idType, savedUser.getId());
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
}