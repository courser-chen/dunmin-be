package com.dunmin.timezone;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 时区工具类
 * 提供时区转换和时间处理功能
 */
@Component
public class TimeZoneHelper {

    private static final ZoneId UTC = ZoneId.of("UTC");
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 默认用户时区（UTC）
     */
    private String defaultUserTimeZone = "UTC";

    public void setDefaultUserTimeZone(String defaultUserTimeZone) {
        this.defaultUserTimeZone = defaultUserTimeZone;
    }

    /**
     * 获取当前 UTC 时间
     */
    public LocalDateTime nowUtc() {
        return LocalDateTime.now(UTC);
    }

    /**
     * 获取当前用户时区时间
     */
    public LocalDateTime nowUserTime(String userTimeZone) {
        ZoneId zone = getZoneId(userTimeZone);
        return LocalDateTime.now(zone);
    }

    /**
     * 获取当前用户时区时间（使用默认时区）
     */
    public LocalDateTime nowUserTime() {
        return nowUserTime(defaultUserTimeZone);
    }

    /**
     * UTC 时间转换为用户时区时间
     */
    public LocalDateTime toUserTime(LocalDateTime utcTime, String userTimeZone) {
        if (utcTime == null) {
            return null;
        }
        ZoneId userZone = getZoneId(userTimeZone);
        ZonedDateTime utc = utcTime.atZone(UTC);
        return utc.withZoneSameInstant(userZone).toLocalDateTime();
    }

    /**
     * 用户时区时间转换为 UTC
     */
    public LocalDateTime toUtc(LocalDateTime userTime, String userTimeZone) {
        if (userTime == null) {
            return null;
        }
        ZoneId userZone = getZoneId(userTimeZone);
        ZonedDateTime user = userTime.atZone(userZone);
        return user.withZoneSameInstant(UTC).toLocalDateTime();
    }

    /**
     * UTC Date 转换为用户时区 Date
     */
    public Date toUserDate(Date utcDate, String userTimeZone) {
        if (utcDate == null) {
            return null;
        }
        Instant instant = utcDate.toInstant();
        ZoneId userZone = getZoneId(userTimeZone);
        return Date.from(instant.atZone(userZone).toInstant());
    }

    /**
     * 用户时区 Date 转换为 UTC Date
     */
    public Date toUtcDate(Date userDate, String userTimeZone) {
        if (userDate == null) {
            return null;
        }
        ZonedDateTime userZoned = userDate.toInstant().atZone(getZoneId(userTimeZone));
        return Date.from(userZoned.withZoneSameInstant(UTC).toInstant());
    }

    /**
     * 格式化时间（使用指定时区）
     */
    public String format(LocalDateTime dateTime, String timeZone) {
        if (dateTime == null) {
            return null;
        }
        ZoneId zone = getZoneId(timeZone);
        ZonedDateTime zonedDateTime = dateTime.atZone(zone);
        return zonedDateTime.format(DEFAULT_FORMATTER);
    }

    /**
     * 格式化时间（使用默认时区）
     */
    public String format(LocalDateTime dateTime) {
        return format(dateTime, defaultUserTimeZone);
    }

    /**
     * 解析时间字符串
     */
    public LocalDateTime parse(String dateTimeStr, String timeZone) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        LocalDateTime ldt = LocalDateTime.parse(dateTimeStr, DEFAULT_FORMATTER);
        // 假设输入时间是用户时区
        return toUtc(ldt, timeZone);
    }

    /**
     * 解析 UTC 时间字符串
     */
    public LocalDateTime parseUtc(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DEFAULT_FORMATTER);
    }

    /**
     * 获取支持的时区列表（常用）
     */
    public List<TimeZoneInfo> getSupportedTimeZones() {
        return Arrays.asList(
            new TimeZoneInfo("UTC", "UTC (世界协调时间)", "UTC"),
            new TimeZoneInfo("Asia/Shanghai", "中国 (UTC+8)", "Asia/Shanghai"),
            new TimeZoneInfo("Asia/Tokyo", "日本 (UTC+9)", "Asia/Tokyo"),
            new TimeZoneInfo("Asia/Seoul", "韩国 (UTC+9)", "Asia/Seoul"),
            new TimeZoneInfo("Asia/Singapore", "新加坡 (UTC+8)", "Asia/Singapore"),
            new TimeZoneInfo("America/New_York", "美国东部 (UTC-5)", "America/New_York"),
            new TimeZoneInfo("America/Los_Angeles", "美国太平洋 (UTC-8)", "America/Los_Angeles"),
            new TimeZoneInfo("Europe/London", "英国 (UTC+0)", "Europe/London"),
            new TimeZoneInfo("Europe/Paris", "法国 (UTC+1)", "Europe/Paris"),
            new TimeZoneInfo("Europe/Berlin", "德国 (UTC+1)", "Europe/Berlin"),
            new TimeZoneInfo("Australia/Sydney", "澳大利亚 (UTC+10)", "Australia/Sydney"),
            new TimeZoneInfo("Asia/Hong_Kong", "香港 (UTC+8)", "Asia/Hong_Kong")
        );
    }

    /**
     * 获取所有可用时区
     */
    public Set<String> getAllTimeZones() {
        return ZoneId.getAvailableZoneIds();
    }

    /**
     * 验证时区是否有效
     */
    public boolean isValidTimeZone(String timeZone) {
        try {
            ZoneId.of(timeZone);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取指定时区与 UTC 的时差（小时）
     */
    public int getUtcOffset(String timeZone) {
        ZoneId zone = getZoneId(timeZone);
        ZonedDateTime now = ZonedDateTime.now(zone);
        return now.getOffset().getTotalSeconds() / 3600;
    }

    /**
     * 获取 ZoneId 对象
     */
    private ZoneId getZoneId(String timeZone) {
        if (timeZone == null || timeZone.isEmpty()) {
            return UTC;
        }
        try {
            return ZoneId.of(timeZone);
        } catch (Exception e) {
            return UTC;
        }
    }

    /**
     * 时区信息类
     */
    public static class TimeZoneInfo {
        private String id;
        private String displayName;
        private String zoneId;

        public TimeZoneInfo(String id, String displayName, String zoneId) {
            this.id = id;
            this.displayName = displayName;
            this.zoneId = zoneId;
        }

        public String getId() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getZoneId() {
            return zoneId;
        }
    }
}
