package com.dunmin.cache;

import com.alibaba.fastjson2.TypeReference;

import java.util.List;

/**
 * 缓存接口
 */
public interface Cache {

    /**
     * 获取缓存值
     *
     * @param key 缓存键
     * @return 缓存值，不存在返回 null
     */
    Object get(String key);

    /**
     * 获取缓存值，并转换为指定类型
     *
     * @param key   缓存键
     * @param clazz 目标类型
     * @param <T>   泛型
     * @return 缓存值，不存在返回 null
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 获取缓存值，使用 TypeReference 指定复杂类型
     *
     * @param key     缓存键
     * @param typeRef 类型引用
     * @param <T>     泛型
     * @return 缓存值，不存在返回 null
     */
    <T> T get(String key, TypeReference<T> typeRef);

    /**
     * 获取 List 类型的缓存
     *
     * @param key   缓存键
     * @param clazz 元素类型
     * @param <T>   泛型
     * @return 缓存值，不存在返回 null
     */
    <T> List<T> getList(String key, Class<T> clazz);

    /**
     * 设置缓存值
     *
     * @param key   缓存键
     * @param value 缓存值
     */
    void put(String key, Object value);

    /**
     * 设置缓存值，指定过期时间
     *
     * @param key        缓存键
     * @param value      缓存值
     * @param expireTime 过期时间（秒）
     */
    void put(String key, Object value, long expireTime);

    /**
     * 删除缓存
     *
     * @param key 缓存键
     */
    void delete(String key);

    /**
     * 检查缓存是否存在
     *
     * @param key 缓存键
     * @return 是否存在
     */
    boolean exists(String key);
}
