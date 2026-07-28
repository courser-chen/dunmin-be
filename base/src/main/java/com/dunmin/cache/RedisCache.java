package com.dunmin.cache;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.List;

/**
 * Redis 缓存实现
 */
public class RedisCache implements Cache {

    private final StringRedisTemplate redisTemplate;
    private static final long DEFAULT_EXPIRE_TIME = 3600; // 默认 1 小时

    public RedisCache(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Object get(String key) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return JSON.parse(value);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return JSON.parseObject(value, clazz);
    }

    @Override
    public <T> T get(String key, TypeReference<T> typeRef) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return JSON.parseObject(value, typeRef.getType());
    }

    @Override
    public <T> List<T> getList(String key, Class<T> clazz) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return JSON.parseArray(value, clazz);
    }

    @Override
    public void put(String key, Object value) {
        put(key, value, DEFAULT_EXPIRE_TIME);
    }

    @Override
    public void put(String key, Object value, long expireTime) {
        String jsonValue = JSON.toJSONString(value);
        redisTemplate.opsForValue().set(key, jsonValue, Duration.ofSeconds(expireTime));
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
