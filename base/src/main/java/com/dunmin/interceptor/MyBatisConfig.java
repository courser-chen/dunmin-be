package com.dunmin.interceptor;

import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis 拦截器配置
 */
@Configuration
public class MyBatisConfig {

    /**
     * 注册查询拦截器
     * 包含逻辑删除和审计字段处理
     */
    @Bean
    public Interceptor queryInterceptor() {
        return new QueryInterceptor();
    }
}
