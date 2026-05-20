package com.example.litelog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SmsService {

    private final Map<String, String> smsCodeCache = new ConcurrentHashMap<>();
    private final Map<String, Long> smsSendTimeCache = new ConcurrentHashMap<>();
    private static final long SMS_INTERVAL_MS = 60000;

    public boolean sendSMSCode(String phone, String type) {
        Long lastSendTime = smsSendTimeCache.get(phone);
        if (lastSendTime != null && System.currentTimeMillis() - lastSendTime < SMS_INTERVAL_MS) {
            log.warn("短信发送过于频繁: {}", phone);
            return false;
        }

        String code = generateCode();
        smsCodeCache.put(phone, code);
        smsSendTimeCache.put(phone, System.currentTimeMillis());

        log.info("发送验证码到手机号: {}, 验证码: {}, 类型: {}", phone, code, type);

        return true;
    }

    public boolean verifyCode(String phone, String code) {
        String cachedCode = smsCodeCache.get(phone);
        if (cachedCode == null) {
            return false;
        }

        boolean valid = cachedCode.equals(code);
        
        if (valid) {
            smsCodeCache.remove(phone);
        }

        return valid;
    }

    private String generateCode() {
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
}