package com.dunmin.timezone;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 时区 MVC 配置
 * 注册时区拦截器和参数解析器
 */
@Configuration
public class TimeZoneMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册时区拦截器
        TimeZoneInterceptor timeZoneInterceptor = new TimeZoneInterceptor();
        timeZoneInterceptor.setDefaultTimeZone("UTC");
        
        registry.addInterceptor(timeZoneInterceptor)
                .addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(java.util.List<org.springframework.web.method.support.HandlerMethodArgumentResolver> resolvers) {
        // 注册时区参数解析器
        resolvers.add(new TimeZoneHandlerMethodArgumentResolver());
    }
}
