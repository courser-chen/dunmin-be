package com.dunmin.model.entity.sys;

import com.dunmin.repository.entity.BaseEntity;
import com.mybatisflex.annotation.Table;

/**
 * 用户角色关联实体
 */
@Table(value = "sys_user_role")
public class UserRole extends BaseEntity {

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 角色 ID
     */
    private Long roleId;

    // getter/setter
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
