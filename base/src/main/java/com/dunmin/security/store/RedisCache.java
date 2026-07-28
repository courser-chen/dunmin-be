package com.dunmin.security.store;

import com.alibaba.fastjson2.JSON;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;


public class RedisCache implements Cache {

    private RedisTemplate redisTemplate;

    public RedisCache(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void put(String key, Object value) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
    }


    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public <T> T get(String key, Class<T> type) {
       Object content = redisTemplate.opsForValue().get(key);
       return JSON.parseObject((String)content, type);
    }

    @Override
    public <T> List<T> getList(String key, Class<T> type) {
        return JSON.parseArray((String)redisTemplate.opsForValue().get(key), type);
    }


}
