package com.dunmin.sys.service;

import com.dunmin.model.entity.sys.Role;
import com.dunmin.repository.service.BaseServiceImpl;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色 Service
 */
@Service
public class RoleService extends BaseServiceImpl<Role> {

    @Autowired
    private com.dunmin.sys.repository.RoleMapper roleMapper;

    @Override
    protected BaseMapper<Role> getMapper() {
        return roleMapper;
    }

    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }

    /**
     * 查询所有角色
     */
    public List<Role> listAll() {
        return listByQuery(
            QueryWrapper.create()
                .from(Role.class)
                .orderBy("sort asc")
        );
    }

    /**
     * 根据角色编码查询
     */
    public Role getByCode(String code) {
        return getOneByQuery(
            QueryWrapper.create()
                .from(Role.class)
                .and("code = ?", code)
        );
    }

    /**
     * 检查角色编码是否存在
     */
    public boolean existsByCode(String code) {
        return getByCode(code) != null;
    }
}
