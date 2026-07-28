package com.dunmin.sys.service;

import com.dunmin.model.entity.sys.User;
import com.dunmin.repository.service.BaseServiceImpl;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户 Service
 */
@Service
public class UserService extends BaseServiceImpl<User> {

    @Autowired
    private com.dunmin.sys.repository.UserMapper userMapper;

    @Override
    protected BaseMapper<User> getMapper() {
        return userMapper;
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    /**
     * 查询所有用户
     */
    public List<User> listAll() {
        return listByQuery(
            QueryWrapper.create()
                .from(User.class)
                .orderBy("id desc")
        );
    }

    /**
     * 根据用户名查询
     */
    public User getByUsername(String username) {
        return getOneByQuery(
            QueryWrapper.create()
                .from(User.class)
                .and("username = ?", username)
        );
    }

    /**
     * 根据部门查询用户列表
     */
    public List<User> listByDeptId(Long deptId) {
        return listByQuery(
            QueryWrapper.create()
                .from(User.class)
                .and("department_id = ?", deptId)
                .orderBy("id asc")
        );
    }

    /**
     * 检查用户名是否存在
     */
    public boolean existsByUsername(String username) {
        return getByUsername(username) != null;
    }
}
