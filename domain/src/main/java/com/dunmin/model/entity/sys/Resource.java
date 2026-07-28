package com.dunmin.model.entity.sys;

import com.dunmin.repository.entity.BaseEntity;
import com.mybatisflex.annotation.Table;

@Table(value = "sys_resource")
public class Resource extends BaseEntity {

    private String code;

    private String url;

    private Integer type;

    private Long moduleId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }
}
