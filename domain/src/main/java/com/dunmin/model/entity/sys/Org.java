package com.dunmin.model.entity.sys;

import com.dunmin.repository.entity.OrgEntity;
import com.mybatisflex.annotation.Table;

/**
 * 组织实体
 */
@Table(value = "sys_org")
public class Org extends OrgEntity {

    /**
     * 组织名称
     */
    private String name;

    /**
     * 组织编码
     */
    private String code;

    /**
     * 父组织 ID
     */
    private Long parentId;

    /**
     * 祖级列表（如：/1/2/3/）
     */
    private String ancestors;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

    // getter/setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
