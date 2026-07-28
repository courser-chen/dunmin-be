package com.dunmin.timezone;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 时区拦截器
 * 从请求中获取时区信息并设置到上下文
 */
public class TimeZoneInterceptor implements HandlerInterceptor {

    private static final String TIME_ZONE_HEADER = "X-Time-Zone";
    private static final String TIME_ZONE_PARAM = "timeZone";
    private static final String DEFAULT_TIME_ZONE = "UTC";

    /**
     * 默认时区（如果请求中未指定）
     */
    private String defaultTimeZone = DEFAULT_TIME_ZONE;

    public void setDefaultTimeZone(String defaultTimeZone) {
        this.defaultTimeZone = defaultTimeZone;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String timeZone = extractTimeZone(request);
        UserTimeZoneContext.setTimeZone(timeZone);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求完成后清除上下文
        UserTimeZoneContext.clear();
    }

    /**
     * 从请求中提取时区信息
     * 优先级：请求头 > Cookie > 请求参数
     */
    private String extractTimeZone(HttpServletRequest request) {
        // 1. 从请求头获取
        String timeZone = request.getHeader(TIME_ZONE_HEADER);
        if (isValidTimeZone(timeZone)) {
            return timeZone;
        }

        // 2. 从请求参数获取
        timeZone = request.getParameter(TIME_ZONE_PARAM);
        if (isValidTimeZone(timeZone)) {
            return timeZone;
        }

        // 3. 从 Cookie 获取
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("timeZone".equals(cookie.getName())) {
                    timeZone = cookie.getValue();
                    if (isValidTimeZone(timeZone)) {
                        return timeZone;
                    }
                }
            }
        }

        // 4. 返回默认值
        return defaultTimeZone;
    }

    /**
     * 验证时区是否有效
     */
    private boolean isValidTimeZone(String timeZone) {
        if (timeZone == null || timeZone.isEmpty()) {
            return false;
        }
        try {
            java.time.ZoneId.of(timeZone);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
