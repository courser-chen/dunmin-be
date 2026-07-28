package com.dunmin.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 缓存配置
 */
@Configuration
public class CacheConfig {

    @Bean
    public Cache cache(@Autowired StringRedisTemplate redisTemplate) {
        return new RedisCache(redisTemplate);
    }
}
