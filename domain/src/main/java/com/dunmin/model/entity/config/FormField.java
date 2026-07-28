package com.dunmin.model.entity.config;

import com.dunmin.repository.entity.BaseEntity;
import com.mybatisflex.annotation.Table;

@Table(value = "cfg_form_field")
public class FormField extends BaseEntity {

    /**
     * 表单ID
     */
    private Long formId;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段编码
     */
    private String code;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 是否启用查询
     */
    private Integer enableQuery;

    /**
     * 查询条件：1 等于 2 时间区间 3 模糊查询
     */
    private Integer condition;

    /**
     * 排序
     */
    private Integer sort;



    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

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

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Integer getEnableQuery() {
        return enableQuery;
    }

    public void setEnableQuery(Integer enableQuery) {
        this.enableQuery = enableQuery;
    }

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


}
