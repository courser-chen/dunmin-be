package com.dunmin.model.entity.sys;

import com.dunmin.repository.entity.BaseEntity;
import com.mybatisflex.annotation.Table;

@Table(value = "sys_module")
public class Module extends BaseEntity {

    private String name;

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
