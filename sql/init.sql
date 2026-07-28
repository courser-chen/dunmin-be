-- =============================================
-- Dunmin 基础表结构
-- 数据库: MySQL 8.0+
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS dunmin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE dunmin;

-- =============================================
-- 组织表 (sys_org)
-- =============================================
DROP TABLE IF EXISTS sys_org;
CREATE TABLE sys_org (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(100) NOT NULL COMMENT '组织名称',
    code VARCHAR(50) NOT NULL COMMENT '组织编码',
    parent_id BIGINT DEFAULT 0 COMMENT '父级ID',
    ancestors VARCHAR(500) DEFAULT '' COMMENT '祖级列表',
    sort INT DEFAULT 0 COMMENT '排序',
    leader VARCHAR(50) COMMENT '负责人',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    status TINYINT DEFAULT 0 COMMENT '状态: 0-正常, 1-停用',
    dr TINYINT DEFAULT 0 COMMENT '删除标志: 0-正常, 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    creator BIGINT COMMENT '创建者',
    modifier BIGINT COMMENT '修改者',
    UNIQUE KEY uk_code (code),
    KEY idx_parent_id (parent_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组织表';

-- =============================================
-- 部门表 (sys_dept)
-- =============================================
DROP TABLE IF EXISTS sys_dept;
CREATE TABLE sys_dept (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    org_id BIGINT NOT NULL COMMENT '组织ID',
    name VARCHAR(100) NOT NULL COMMENT '部门名称',
    code VARCHAR(50) NOT NULL COMMENT '部门编码',
    parent_id BIGINT DEFAULT 0 COMMENT '父级ID',
    ancestors VARCHAR(500) DEFAULT '' COMMENT '祖级列表',
    sort INT DEFAULT 0 COMMENT '排序',
    leader VARCHAR(50) COMMENT '负责人',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    status TINYINT DEFAULT 0 COMMENT '状态: 0-正常, 1-停用',
    dr TINYINT DEFAULT 0 COMMENT '删除标志: 0-正常, 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    creator BIGINT COMMENT '创建者',
    modifier BIGINT COMMENT '修改者',
    UNIQUE KEY uk_code (code),
    KEY idx_org_id (org_id),
    KEY idx_parent_id (parent_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- =============================================
-- 用户表 (sys_user)
-- =============================================
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    org_id BIGINT COMMENT '组织ID',
    department_id BIGINT COMMENT '部门ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像',
    sex TINYINT COMMENT '性别: 0-未知, 1-男, 2-女',
    status TINYINT DEFAULT 0 COMMENT '状态: 0-正常, 1-停用',
    login_ip VARCHAR(50) COMMENT '最后登录IP',
    login_date DATETIME COMMENT '最后登录时间',
    dr TINYINT DEFAULT 0 COMMENT '删除标志: 0-正常, 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    creator BIGINT COMMENT '创建者',
    modifier BIGINT COMMENT '修改者',
    UNIQUE KEY uk_username (username),
    KEY idx_org_id (org_id),
    KEY idx_department_id (department_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =============================================
-- 角色表 (sys_role)
-- =============================================
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(50) NOT NULL COMMENT '角色名称',
    code VARCHAR(50) NOT NULL COMMENT '角色编码',
    description VARCHAR(255) COMMENT '角色描述',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 0 COMMENT '状态: 0-正常, 1-停用',
    dr TINYINT DEFAULT 0 COMMENT '删除标志: 0-正常, 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    creator BIGINT COMMENT '创建者',
    modifier BIGINT COMMENT '修改者',
    UNIQUE KEY uk_code (code),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- =============================================
-- 用户角色关联表 (sys_user_role)
-- =============================================
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    dr TINYINT DEFAULT 0 COMMENT '删除标志: 0-正常, 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    creator BIGINT COMMENT '创建者',
    modifier BIGINT COMMENT '修改者',
    UNIQUE KEY uk_user_role (user_id, role_id),
    KEY idx_user_id (user_id),
    KEY idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- =============================================
-- 表单配置表 (cfg_form)
-- =============================================
DROP TABLE IF EXISTS cfg_form;
CREATE TABLE cfg_form (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(100) NOT NULL COMMENT '表单名称',
    code VARCHAR(50) NOT NULL COMMENT '表单编码',
    description VARCHAR(255) COMMENT '表单描述',
    table_name VARCHAR(100) COMMENT '关联表名',
    status TINYINT DEFAULT 0 COMMENT '状态: 0-正常, 1-停用',
    dr TINYINT DEFAULT 0 COMMENT '删除标志: 0-正常, 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    creator BIGINT COMMENT '创建者',
    modifier BIGINT COMMENT '修改者',
    UNIQUE KEY uk_code (code),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='表单配置表';

-- =============================================
-- 表单字段表 (cfg_form_field)
-- =============================================
DROP TABLE IF EXISTS cfg_form_field;
CREATE TABLE cfg_form_field (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    form_id BIGINT NOT NULL COMMENT '表单ID',
    field_name VARCHAR(50) NOT NULL COMMENT '字段名称',
    field_code VARCHAR(50) NOT NULL COMMENT '字段编码',
    field_type VARCHAR(20) NOT NULL COMMENT '字段类型: text, number, date, select, radio, checkbox, textarea',
    field_length INT COMMENT '字段长度',
    default_value VARCHAR(255) COMMENT '默认值',
    placeholder VARCHAR(100) COMMENT '占位符',
    options VARCHAR(500) COMMENT '选项值(逗号分隔)',
    is_required TINYINT DEFAULT 0 COMMENT '是否必填: 0-否, 1-是',
    is_unique TINYINT DEFAULT 0 COMMENT '是否唯一: 0-否, 1-是',
    is_display TINYINT DEFAULT 1 COMMENT '是否显示: 0-隐藏, 1-显示',
    sort INT DEFAULT 0 COMMENT '排序',
    validation_rule VARCHAR(255) COMMENT '验证规则',
    dr TINYINT DEFAULT 0 COMMENT '删除标志: 0-正常, 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    creator BIGINT COMMENT '创建者',
    modifier BIGINT COMMENT '修改者',
    UNIQUE KEY uk_form_field (form_id, field_code),
    KEY idx_form_id (form_id),
    KEY idx_sort (sort)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='表单字段表';

-- =============================================
-- 初始化数据
-- =============================================

-- 插入超级管理员角色
INSERT INTO sys_role (id, name, code, description, sort, status) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '系统超级管理员，拥有所有权限', 1, 0);

-- 插入默认组织
INSERT INTO sys_org (id, name, code, parent_id, ancestors, sort, status) VALUES
(1, '总公司', 'ROOT', 0, '0', 1, 0);

-- 插入默认部门
INSERT INTO sys_dept (id, org_id, name, code, parent_id, ancestors, sort, status) VALUES
(1, 1, '研发部', 'RD', 0, '0', 1, 0),
(2, 1, '测试部', 'QA', 0, '0', 2, 0),
(3, 1, '运维部', 'OPS', 0, '0', 3, 0);

-- 插入默认用户 (密码: admin123)
INSERT INTO sys_user (id, org_id, department_id, username, password, nickname, email, phone, sex, status) VALUES
(1, 1, 1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '管理员', 'admin@dunmin.com', '13800138000', 1, 0);

-- 关联用户角色
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 插入示例表单
INSERT INTO cfg_form (id, name, code, description, status) VALUES
(1, '用户信息表单', 'user_info', '用户信息采集表单', 0),
(2, '请假申请表单', 'leave_apply', '员工请假申请表单', 0);

-- 插入示例表单字段
INSERT INTO cfg_form_field (form_id, field_name, field_code, field_type, field_length, is_required, sort) VALUES
(1, '姓名', 'name', 'text', 50, 1, 1),
(1, '手机号', 'phone', 'text', 20, 1, 2),
(1, '邮箱', 'email', 'text', 100, 0, 3),
(1, '性别', 'sex', 'radio', 1, 1, 4),
(1, '生日', 'birthday', 'date', 0, 0, 5),
(1, '部门', 'department', 'select', 50, 1, 6),
(1, '备注', 'remark', 'textarea', 500, 0, 7);
