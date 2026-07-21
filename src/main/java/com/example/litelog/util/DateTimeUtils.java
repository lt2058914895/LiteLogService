package com.example.litelog.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 时间转换工具类，统一处理 epoch seconds 与 LocalDateTime 之间的转换
 */
public final class DateTimeUtils {

    private DateTimeUtils() {
    }

    private static final ZoneId SYSTEM_ZONE = ZoneId.systemDefault();

    /**
     * 将 epoch seconds 转换为 LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Long epochSecond) {
        if (epochSecond == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), SYSTEM_ZONE);
    }

    /**
     * 将 LocalDateTime 转换为 epoch seconds
     */
    public static Long toEpochSeconds(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atZone(SYSTEM_ZONE).toEpochSecond();
    }
}