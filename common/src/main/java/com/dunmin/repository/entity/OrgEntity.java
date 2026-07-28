package com.dunmin.repository.entity;

/**
 * 多组织实体基类
 * 支持组织隔离
 */
public abstract class OrgEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 组织 ID
     */
    private Long orgId;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
