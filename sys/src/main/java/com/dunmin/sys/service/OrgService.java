package com.dunmin.sys.service;

import com.dunmin.model.entity.sys.Org;
import com.dunmin.repository.service.BaseServiceImpl;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 组织 Service
 */
@Service
public class OrgService extends BaseServiceImpl<Org> {

    @Autowired
    private com.dunmin.sys.repository.OrgMapper orgMapper;

    @Override
    protected BaseMapper<Org> getMapper() {
        return orgMapper;
    }

    @Override
    protected Class<Org> getEntityClass() {
        return Org.class;
    }

    /**
     * 查询所有组织
     */
    public List<Org> listAll() {
        return listByQuery(
            QueryWrapper.create()
                .from(Org.class)
                .orderBy("sort asc")
        );
    }

    /**
     * 查询子组织列表
     */
    public List<Org> listByParentId(Long parentId) {
        return listByQuery(
            QueryWrapper.create()
                .from(Org.class)
                .and("parent_id = ?", parentId)
                .orderBy("sort asc")
        );
    }
}
