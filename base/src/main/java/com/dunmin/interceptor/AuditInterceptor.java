package com.dunmin.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.Properties;

/**
 * 审计字段拦截器
 * INSERT 时自动设置 dr = 0 和 creator
 * UPDATE 时自动设置 modifier
 */
@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class AuditInterceptor implements Interceptor {

    private static final String DR_FIELD = "dr";
    private static final String CREATOR_FIELD = "creator";
    private static final String MODIFIER_FIELD = "modifier";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = getStatementHandler(invocation);
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();

        MappedStatement ms = getMappedStatement(statementHandler);
        if (ms == null) {
            return invocation.proceed();
        }

        // 获取实体参数
        Object parameterObject = boundSql.getParameterObject();
        if (parameterObject == null) {
            return invocation.proceed();
        }

        // 判断 SQL 类型
        SqlCommandType commandType = ms.getSqlCommandType();

        if (commandType == SqlCommandType.INSERT) {
            // INSERT 时设置 dr = 0
            sql = addDrForInsert(sql);

            // 设置 creator
            if (hasField(parameterObject, CREATOR_FIELD)) {
                Long userId = getCurrentUserId();
                if (userId != null) {
                    setFieldValue(parameterObject, CREATOR_FIELD, userId);
                }
            }
        } else if (commandType == SqlCommandType.UPDATE) {
            // UPDATE 时设置 modifier
            if (hasField(parameterObject, MODIFIER_FIELD)) {
                Long userId = getCurrentUserId();
                if (userId != null) {
                    setFieldValue(parameterObject, MODIFIER_FIELD, userId);
                }
            }
        }

        // 更新 SQL
        if (!sql.equals(boundSql.getSql())) {
            Field sqlField = BoundSql.class.getDeclaredField("sql");
            sqlField.setAccessible(true);
            sqlField.set(boundSql, sql);
        }

        return invocation.proceed();
    }

    /**
     * INSERT 时添加 dr = 0
     */
    private String addDrForInsert(String sql) {
        // 检查是否已经有 dr 字段
        if (sql.toUpperCase().contains("DR")) {
            return sql;
        }

        // 找到 VALUES 前的位置，添加 dr 字段
        String upperSql = sql.toUpperCase();
        int valuesIndex = upperSql.indexOf("VALUES");
        if (valuesIndex > 0) {
            // 在字段列表中添加 dr
            int fieldsEnd = upperSql.indexOf(")", 0, valuesIndex);
            if (fieldsEnd > 0) {
                sql = sql.substring(0, fieldsEnd + 1) + ", dr" + sql.substring(fieldsEnd + 1);

                // 在 VALUES 列表中添加 0
                int valuesParenStart = upperSql.indexOf("(", valuesIndex);
                if (valuesParenStart > 0) {
                    int valuesParenEnd = upperSql.indexOf(")", valuesParenStart);
                    if (valuesParenEnd > 0) {
                        sql = sql.substring(0, valuesParenEnd + 1) + ", 0" + sql.substring(valuesParenEnd + 1);
                    }
                }
            }
        }

        return sql;
    }

    /**
     * 检查对象是否有指定字段
     */
    private boolean hasField(Object obj, String fieldName) {
        if (obj == null) return false;
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            try {
                clazz.getDeclaredField(fieldName);
                return true;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return false;
    }

    /**
     * 设置字段值
     */
    private void setFieldValue(Object obj, String fieldName, Object value) {
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                // 只有字段值为空时才设置
                if (field.get(obj) == null) {
                    field.set(obj, value);
                }
                return;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (IllegalAccessException e) {
                break;
            }
        }
    }

    /**
     * 获取当前用户 ID（需要从上下文获取）
     */
    private Long getCurrentUserId() {
        // TODO: 从安全上下文获取当前用户 ID
        return null;
    }

    private StatementHandler getStatementHandler(Invocation invocation) {
        Object target = invocation.getTarget();
        if (Proxy.isProxyClass(target.getClass())) {
            target = SystemMetaObject.forObject(target).getValue("h");
        }
        return (StatementHandler) target;
    }

    private MappedStatement getMappedStatement(StatementHandler handler) {
        try {
            Field field = StatementHandler.class.getDeclaredField("mappedStatement");
            field.setAccessible(true);
            return (MappedStatement) field.get(handler);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
