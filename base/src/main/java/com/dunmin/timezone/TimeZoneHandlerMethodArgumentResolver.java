package com.dunmin.timezone;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * Spring MVC Controller 方法参数时区转换解析器
 * 
 * 功能：
 * 1. 自动将 String 类型的时间参数从用户时区转换为 UTC
 * 2. 支持 LocalDateTime、LocalDate、LocalTime 类型
 * 
 * 使用方式：
 * 在配置类中注册此解析器，Controller 方法参数使用时间类型即可自动转换
 */
public class TimeZoneHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";

    private static final Pattern DATE_TIME_PATTERN_REGEX = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
    private static final Pattern DATE_PATTERN_REGEX = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
    private static final Pattern TIME_PATTERN_REGEX = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> type = parameter.getParameterType();
        return LocalDateTime.class.isAssignableFrom(type) 
            || LocalDate.class.isAssignableFrom(type) 
            || LocalTime.class.isAssignableFrom(type);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                   NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        
        String parameterName = parameter.getParameterName();
        String requestValue = webRequest.getParameter(parameterName);
        
        if (requestValue == null || requestValue.isEmpty()) {
            return null;
        }
        
        Class<?> type = parameter.getParameterType();
        
        if (LocalDateTime.class.isAssignableFrom(type)) {
            return parseDateTime(requestValue);
        } else if (LocalDate.class.isAssignableFrom(type)) {
            return parseDate(requestValue);
        } else if (LocalTime.class.isAssignableFrom(type)) {
            return parseTime(requestValue);
        }
        
        return null;
    }

    /**
     * 解析日期时间字符串，将用户时区转换为 UTC
     */
    private LocalDateTime parseDateTime(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        // 检查是否匹配日期时间格式
        if (!DATE_TIME_PATTERN_REGEX.matcher(value).matches()) {
            return null;
        }
        
        String userTimeZone = UserTimeZoneContext.getTimeZone();
        LocalDateTime userTime = LocalDateTime.parse(value, DATE_TIME_FORMATTER);
        return toUtc(userTime, userTimeZone);
    }

    /**
     * 解析日期字符串
     */
    private LocalDate parseDate(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        // 检查是否匹配日期格式
        if (!DATE_PATTERN_REGEX.matcher(value).matches()) {
            return null;
        }
        
        return LocalDate.parse(value, DATE_FORMATTER);
    }

    /**
     * 解析时间字符串
     */
    private LocalTime parseTime(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        // 检查是否匹配时间格式
        if (!TIME_PATTERN_REGEX.matcher(value).matches()) {
            return null;
        }
        
        return LocalTime.parse(value, TIME_FORMATTER);
    }

    /**
     * 将用户时区时间转换为 UTC
     */
    private LocalDateTime toUtc(LocalDateTime userTime, String timeZone) {
        if (userTime == null) return null;
        ZoneId userZone = ZoneId.of(timeZone);
        ZoneId utcZone = ZoneId.of("UTC");
        return userTime.atZone(userZone).withZoneSameInstant(utcZone).toLocalDateTime();
    }
}
