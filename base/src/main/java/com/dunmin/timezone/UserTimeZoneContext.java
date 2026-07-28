package com.dunmin.timezone;

/**
 * 用户时区上下文
 * 用于在当前线程中存储和获取用户时区信息
 */
public class UserTimeZoneContext {

    private static final ThreadLocal<String> USER_TIME_ZONE = new ThreadLocal<>();
    private static final String DEFAULT_TIME_ZONE = "UTC";

    /**
     * 设置当前用户的时区
     *
     * @param timeZone 时区 ID（如：Asia/Shanghai）
     */
    public static void setTimeZone(String timeZone) {
        if (timeZone == null || timeZone.isEmpty()) {
            USER_TIME_ZONE.set(DEFAULT_TIME_ZONE);
        } else {
            USER_TIME_ZONE.set(timeZone);
        }
    }

    /**
     * 获取当前用户的时区
     *
     * @return 时区 ID，默认为 UTC
     */
    public static String getTimeZone() {
        String timeZone = USER_TIME_ZONE.get();
        return timeZone != null ? timeZone : DEFAULT_TIME_ZONE;
    }

    /**
     * 检查是否已设置时区
     */
    public static boolean hasTimeZone() {
        return USER_TIME_ZONE.get() != null;
    }

    /**
     * 清除当前线程的时区信息
     * 建议在请求结束时调用
     */
    public static void clear() {
        USER_TIME_ZONE.remove();
    }

    /**
     * 执行带时区的任务
     *
     * @param timeZone 时区
     * @param task     任务
     * @param <T>      返回值类型
     * @return 任务执行结果
     */
    public static <T> T executeWithTimeZone(String timeZone, java.util.function.Supplier<T> task) {
        String original = getTimeZone();
        try {
            setTimeZone(timeZone);
            return task.get();
        } finally {
            setTimeZone(original);
        }
    }

    /**
     * 执行带时区的任务（无返回值）
     *
     * @param timeZone 时区
     * @param task     任务
     */
    public static void executeWithTimeZone(String timeZone, Runnable task) {
        String original = getTimeZone();
        try {
            setTimeZone(timeZone);
            task.run();
        } finally {
            setTimeZone(original);
        }
    }
}
