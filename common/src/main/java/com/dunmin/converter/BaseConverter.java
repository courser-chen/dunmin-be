package com.dunmin.converter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MapStruct 转换基类
 * 提供常用的对象转换方法
 *
 * @param <S> 源类型
 * @param <T> 目标类型
 */
public interface BaseConverter<S, T> {

    /**
     * 源类型转换为目标类型
     *
     * @param source 源对象
     * @return 目标对象
     */
    T convert(S source);

    /**
     * 批量转换列表
     *
     * @param sources 源对象列表
     * @return 目标对象列表
     */
    default List<T> convert(List<S> sources) {
        if (sources == null) {
            return null;
        }
        return sources.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    /**
     * 批量转换数组
     *
     * @param sources 源对象数组
     * @return 目标对象数组
     */
    default T[] convert(S[] sources) {
        if (sources == null) {
            return null;
        }
        return java.util.Arrays.stream(sources)
                .map(this::convert)
                .toArray(size -> (T[]) java.lang.reflect.Array.newInstance(
                        getTargetClass(), size));
    }

    /**
     * 获取目标类型 Class
     *
     * @return 目标类型 Class
     */
    Class<T> getTargetClass();
}
