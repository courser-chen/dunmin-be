package com.dunmin.security;

public enum ResourceType {
    STATIC(1, "External Static Resources、Internal Resources "),
    API(2, "Api"),
    LINK(3, "Link、 Button");

    private final int code;
    private final String desc;


    ResourceType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
