package com.dunmin.interceptor;

import org.apache.ibatis.plugin.*;

import java.util.Properties;

/**
 * 查询拦截器
 * 1. DrQueryInterceptor - 自动添加 dr = 0 条件
 * 2. AuditInterceptor - 自动设置审计字段
 */
public class QueryInterceptor implements Interceptor {

    private final DefaultQueryInterceptor drQueryInterceptor = new DefaultQueryInterceptor();
    private final AuditInterceptor auditInterceptor = new AuditInterceptor();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 先执行审计拦截器（INSERT/UPDATE 时设置审计字段）
        auditInterceptor.intercept(invocation);

        // 再执行查询拦截器（SELECT 时添加 dr = 0）
        drQueryInterceptor.intercept(invocation);

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
