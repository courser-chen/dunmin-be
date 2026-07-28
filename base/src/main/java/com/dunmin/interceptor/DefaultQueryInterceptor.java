package com.dunmin.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * 逻辑删除查询拦截器
 * 自动在 SELECT 查询中添加 dr = 0 条件
 */
@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class DefaultQueryInterceptor implements Interceptor {

    // 匹配 dr = 0 或 dr =0 或 dr=0 等各种格式
    private static final Pattern DR_CONDITION_PATTERN = Pattern.compile(
        "\\bdr\\s*=\\s*0\\b",
        Pattern.CASE_INSENSITIVE
    );

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = getStatementHandler(invocation);
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        String originalSql = sql;

        // 判断是否为查询语句
        if (isSelectQuery(sql) && !hasDrCondition(sql)) {
            // 获取 Mapper 信息，判断是否为 repository 包下的 Mapper
            if (isRepositoryMapper(invocation)) {
                sql = addDrCondition(sql);
            }
        }

        // 如果 SQL 有变化，更新 BoundSql
        if (!sql.equals(originalSql)) {
            Field sqlField = BoundSql.class.getDeclaredField("sql");
            sqlField.setAccessible(true);
            sqlField.set(boundSql, sql);
        }

        return invocation.proceed();
    }

    /**
     * 判断是否为 SELECT 查询
     */
    private boolean isSelectQuery(String sql) {
        String trimmed = sql.trim().toUpperCase();
        return trimmed.startsWith("SELECT") && !trimmed.startsWith("SELECT FOR UPDATE");
    }

    /**
     * 检查 SQL 是否已经包含 dr = 0 条件
     */
    private boolean hasDrCondition(String sql) {
        return DR_CONDITION_PATTERN.matcher(sql).find();
    }

    /**
     * 添加 dr = 0 条件
     */
    private String addDrCondition(String sql) {
        String upperSql = sql.toUpperCase();

        // 在 WHERE 后面添加 dr = 0
        int whereIndex = upperSql.lastIndexOf("WHERE");
        if (whereIndex > 0) {
            int insertIndex = whereIndex + 5;
            // 找到 WHERE 后面的位置，跳过空白
            while (insertIndex < sql.length() && Character.isWhitespace(sql.charAt(insertIndex))) {
                insertIndex++;
            }
            return sql.substring(0, insertIndex) + "dr = 0 AND " + sql.substring(insertIndex);
        }

        // 如果没有 WHERE，检查是否有 ORDER BY
        int orderIndex = upperSql.lastIndexOf("ORDER BY");
        if (orderIndex > 0) {
            return sql.substring(0, orderIndex) + "WHERE dr = 0 " + sql.substring(orderIndex);
        }

        // 如果没有 ORDER BY，检查是否有 LIMIT
        int limitIndex = upperSql.lastIndexOf("LIMIT");
        if (limitIndex > 0) {
            return sql.substring(0, limitIndex) + "WHERE dr = 0 " + sql.substring(limitIndex);
        }

        // 如果都没有，检查是否有 GROUP BY
        int groupIndex = upperSql.lastIndexOf("GROUP BY");
        if (groupIndex > 0) {
            return sql.substring(0, groupIndex) + "WHERE dr = 0 " + sql.substring(groupIndex);
        }

        // 如果都没有，在 FROM 后面添加 WHERE dr = 0
        int fromIndex = upperSql.indexOf("FROM");
        if (fromIndex > 0) {
            int tableEndIndex = fromIndex + 4;
            // 找到 FROM 后面的第一个空白
            while (tableEndIndex < sql.length() && Character.isWhitespace(sql.charAt(tableEndIndex))) {
                tableEndIndex++;
            }
            // 找到表名结束位置
            int nextKeyword = findNextKeyword(sql, tableEndIndex);
            return sql.substring(0, nextKeyword) + "WHERE dr = 0" + sql.substring(nextKeyword);
        }

        return sql;
    }

    /**
     * 查找下一个关键字的位置
     */
    private int findNextKeyword(String sql, int start) {
        String[] keywords = {"WHERE", "ORDER", "GROUP", "LIMIT", "OFFSET", "HAVING"};
        int earliestIndex = sql.length();

        for (String keyword : keywords) {
            int index = sql.toUpperCase().indexOf(keyword, start);
            if (index > 0 && index < earliestIndex) {
                earliestIndex = index;
            }
        }

        return earliestIndex;
    }

    /**
     * 判断是否为 repository 包下的 Mapper
     */
    private boolean isRepositoryMapper(Invocation invocation) {
        try {
            StatementHandler handler = getStatementHandler(invocation);
            MappedStatement ms = getMappedStatement(handler);
            if (ms == null) {
                return false;
            }

            String msId = ms.getId();
            if (msId == null) {
                return false;
            }

            // 检查 Mapper ID 是否包含 repository
            return msId.contains(".repository.");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取 StatementHandler
     */
    private StatementHandler getStatementHandler(Invocation invocation) {
        Object target = invocation.getTarget();
        if (Proxy.isProxyClass(target.getClass())) {
            target = SystemMetaObject.forObject(target).getValue("h");
        }
        return (StatementHandler) target;
    }

    /**
     * 获取 MappedStatement
     */
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
        // 可以从配置文件读取
    }
}
