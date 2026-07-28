package com.dunmin.model.entity.config;

import com.dunmin.repository.entity.BaseEntity;
import com.mybatisflex.annotation.Table;

@Table(value = "cfg_form")
public class Form extends BaseEntity {

    /**
     * 表单名称
     */
    private String name;

    /**
     * 表单编码
     */
    private String code;

    /**
     * 表单关联的实体类
     */
    private String entityClass;

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

    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }
}
