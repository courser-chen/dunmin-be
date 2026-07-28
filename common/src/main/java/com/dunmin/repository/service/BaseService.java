package com.dunmin.repository.service;

import com.dunmin.repository.entity.BaseEntity;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;

import java.io.Serializable;
import java.util.List;

/**
 * Service 基类接口，定义通用 CRUD 操作
 *
 * @param <T> 实体类型
 */
public interface BaseService<T extends BaseEntity> {

    /**
     * 根据 ID 查询
     *
     * @param id 主键 ID
     * @return 实体对象
     */
    T getById(Serializable id);

    /**
     * 条件分页查询
     *
     * @param page        页码（从 1 开始）
     * @param pageSize    每页大小
     * @param queryWrapper 查询条件
     * @return 分页结果
     */
    Page<T> page(int page, int pageSize, QueryWrapper queryWrapper);

    /**
     * 根据条件查询列表
     *
     * @param queryWrapper 查询条件
     * @return 实体列表
     */
    List<T> listByQuery(QueryWrapper queryWrapper);

    /**
     * 根据条件查询单个对象
     *
     * @param queryWrapper 查询条件
     * @return 实体对象
     */
    T getOneByQuery(QueryWrapper queryWrapper);

    /**
     * 保存数据（新增或更新）
     * 如果实体有 id 则更新，否则新增
     *
     * @param entity 实体对象
     * @return 是否成功
     */
    boolean save(T entity);

    /**
     * 批量保存数据（新增或更新）
     *
     * @param entities 实体列表
     * @return 影响行数
     */
    int saveBatch(List<T> entities);

    /**
     * 根据 ID 删除（物理删除）
     *
     * @param id 主键 ID
     * @return 是否成功
     */
    boolean deleteById(Serializable id);

    /**
     * 根据 ID 删除（逻辑删除）
     *
     * @param id 主键 ID
     * @return 是否成功
     */
    boolean removeById(Serializable id);

    /**
     * 根据条件删除（逻辑删除）
     *
     * @param queryWrapper 删除条件
     * @return 是否成功
     */
    boolean removeByQuery(QueryWrapper queryWrapper);

    /**
     * 批量删除（物理删除）
     *
     * @param ids 主键 ID 列表
     * @return 是否成功
     */
    boolean deleteByIds(List<? extends Serializable> ids);

    /**
     * 批量删除（逻辑删除）
     *
     * @param ids 主键 ID 列表
     * @return 是否成功
     */
    boolean removeByIds(List<? extends Serializable> ids);

    /**
     * 统计数量
     *
     * @return 数量
     */
    long count();

    /**
     * 根据条件统计数量
     *
     * @param queryWrapper 查询条件
     * @return 数量
     */
    long countByQuery(QueryWrapper queryWrapper);

    /**
     * 判断是否存在
     *
     * @param id 主键 ID
     * @return 是否存在
     */
    boolean existsById(Serializable id);

    /**
     * 判断条件是否存在
     *
     * @param queryWrapper 查询条件
     * @return 是否存在
     */
    boolean existsByQuery(QueryWrapper queryWrapper);
}
