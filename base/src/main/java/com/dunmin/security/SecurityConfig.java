package com.dunmin.security;

import com.dunmin.security.auth.AuthBuilder;
import com.dunmin.security.auth.AuthHandler;
import com.dunmin.security.auth.AuthService;
import com.dunmin.security.auth.WebAuthHandler;
import com.dunmin.security.boot.CustomAuthenticationProvider;
import com.dunmin.security.interceptor.AuthorizeInterceptor;
import com.dunmin.security.store.Cache;
import com.dunmin.security.store.SessionCache;
import com.dunmin.security.store.RedisCache;
import com.dunmin.security.store.PrincipalStoreImpl;
import com.dunmin.security.store.PrincipalStore;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 权限管理的配置类
 */
@Configuration
@EnableWebSecurity
@ConditionalOnBean({Authenticator.class, Authorizer.class})
@Import({PrincipalStoreImpl.class, SecurityContext.class, AuthorizeInterceptor.class, CustomAuthenticationProvider.class})
public class SecurityConfig implements ApplicationContextAware, WebMvcConfigurer {

    private ApplicationContext applicationContext;

    @Autowired
    private AuthorizeInterceptor authorizeInterceptor;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizeInterceptor)
                .addPathPatterns("/**");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            .authorizeHttpRequests(auth -> 
                auth
                    .requestMatchers("/login", "/logout").permitAll()
                    .anyRequest().authenticated()
            )
            .authenticationProvider(customAuthenticationProvider);
        
        return http.build();
    }

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public Cache redisCacheStore(@Autowired RedisTemplate<String,String> redisTemplate) {
        return new RedisCache(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    public Cache sessionCacheStore() {
        return new SessionCache();
    }

    /**
     * 构建验证服务
     */
    @Bean
    @ConditionalOnMissingBean(AuthService.class)
    @ConditionalOnBean(AuthHandler.class)
    public AuthService loginService(@Autowired AuthHandler authHandler){
        return AuthBuilder.build().addHandler(authHandler).enableLogin(applicationContext);
    }

    /**
     * 构建授权服务
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(Authenticator.class)
    public Authorizer getAuthorizer(){
        return null;
    }
}
