package com.dunmin.repository.service;

import com.dunmin.repository.entity.BaseEntity;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;

import java.io.Serializable;
import java.util.List;

/**
 * Service 基类抽象实现
 *
 * @param <T> 实体类型
 */
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    /**
     * 获取 Mapper
     *
     * @return BaseMapper
     */
    protected abstract BaseMapper<T> getMapper();

    /**
     * 获取实体类
     *
     * @return 实体类
     */
    protected abstract Class<T> getEntityClass();

    @Override
    public T getById(Serializable id) {
        return getMapper().selectOneById(id);
    }

    @Override
    public Page<T> page(int page, int pageSize, QueryWrapper queryWrapper) {
        return getMapper().paginate(page, pageSize, queryWrapper);
    }

    @Override
    public List<T> listByQuery(QueryWrapper queryWrapper) {
        return getMapper().selectListByQuery(queryWrapper);
    }

    @Override
    public T getOneByQuery(QueryWrapper queryWrapper) {
        return getMapper().selectOneByQuery(queryWrapper);
    }

    @Override
    public boolean save(T entity) {
        if (entity == null) {
            return false;
        }
        if (entity.getId() != null) {
            return getMapper().update(entity) > 0;
        } else {
            return getMapper().insert(entity, true) > 0;
        }
    }

    @Override
    public int saveBatch(List<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (T entity : entities) {
            count += save(entity) ? 1 : 0;
        }
        return count;
    }

    @Override
    public boolean deleteById(Serializable id) {
        return getMapper().deleteById(id) > 0;
    }

    @Override
    public boolean removeById(Serializable id) {
        T entity = getById(id);
        if (entity != null) {
            entity.setDr(1);
            return getMapper().update(entity) > 0;
        }
        return false;
    }

    @Override
    public boolean removeByQuery(QueryWrapper queryWrapper) {
        T entity = getOneByQuery(queryWrapper);
        if (entity != null) {
            entity.setDr(1);
            return getMapper().update(entity) > 0;
        }
        return false;
    }

    @Override
    public boolean deleteByIds(List<? extends Serializable> ids) {
        return getMapper().deleteBatchByIds(ids) > 0;
    }

    @Override
    public boolean removeByIds(List<? extends Serializable> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        boolean success = true;
        for (Serializable id : ids) {
            if (!removeById(id)) {
                success = false;
            }
        }
        return success;
    }

    @Override
    public long count() {
        return getMapper().selectCountByQuery(QueryWrapper.create().from(getEntityClass()));
    }

    @Override
    public long countByQuery(QueryWrapper queryWrapper) {
        return getMapper().selectCountByQuery(queryWrapper);
    }

    @Override
    public boolean existsById(Serializable id) {
        return getById(id) != null;
    }

    @Override
    public boolean existsByQuery(QueryWrapper queryWrapper) {
        return getOneByQuery(queryWrapper) != null;
    }
}
