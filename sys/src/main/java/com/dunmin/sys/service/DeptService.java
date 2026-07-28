package com.dunmin.sys.service;

import com.dunmin.model.entity.sys.Dept;
import com.dunmin.repository.service.BaseServiceImpl;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门 Service
 */
@Service
public class DeptService extends BaseServiceImpl<Dept> {

    @Autowired
    private com.dunmin.sys.repository.DeptMapper deptMapper;

    @Override
    protected BaseMapper<Dept> getMapper() {
        return deptMapper;
    }

    @Override
    protected Class<Dept> getEntityClass() {
        return Dept.class;
    }

    /**
     * 查询所有部门
     */
    public List<Dept> listAll() {
        return listByQuery(
            QueryWrapper.create()
                .from(Dept.class)
                .orderBy("sort asc")
        );
    }

    /**
     * 查询子部门列表
     */
    public List<Dept> listByParentId(Long parentId) {
        return listByQuery(
            QueryWrapper.create()
                .from(Dept.class)
                .and("parent_id = ?", parentId)
                .orderBy("sort asc")
        );
    }

    /**
     * 根据组织查询部门列表
     */
    public List<Dept> listByOrgId(Long orgId) {
        return listByQuery(
            QueryWrapper.create()
                .from(Dept.class)
                .and("org_id = ?", orgId)
                .orderBy("sort asc")
        );
    }
}
