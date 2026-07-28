package com.dunmin.sys.controller;

import com.dunmin.model.dto.PageDTO;
import com.dunmin.model.entity.sys.Role;
import com.dunmin.sys.service.RoleService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色 Controller
 */
@RestController
@RequestMapping("/role")
@Tag(name = "角色管理")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/{id}")
    @Operation(summary = "获取角色详情")
    public Role getById(@PathVariable Long id) {
        return roleService.getById(id);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "根据角色编码查询")
    public Role getByCode(@PathVariable String code) {
        return roleService.getByCode(code);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询角色")
    public Page<Role> page(PageDTO pageDTO) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .from(Role.class);

        if (pageDTO.getKeyword() != null && !pageDTO.getKeyword().isEmpty()) {
            queryWrapper.and("name like ? or code like ?",
                    "%" + pageDTO.getKeyword() + "%",
                    "%" + pageDTO.getKeyword() + "%");
        }

        if (pageDTO.getOrderBy() != null && !pageDTO.getOrderBy().isEmpty()) {
            queryWrapper.orderBy(pageDTO.getOrderBy(), pageDTO.isAsc());
        } else {
            queryWrapper.orderBy("sort asc");
        }

        return roleService.page(pageDTO.getPage(), pageDTO.getPageSize(), queryWrapper);
    }

    @GetMapping("/list/all")
    @Operation(summary = "查询所有角色")
    public List<Role> listAll() {
        return roleService.listAll();
    }

    @PostMapping
    @Operation(summary = "新增角色")
    public boolean save(@RequestBody Role role) {
        if (role.getId() == null) {
            if (roleService.existsByCode(role.getCode())) {
                throw new RuntimeException("角色编码已存在");
            }
        }
        return roleService.save(role);
    }

    @PostMapping("/update")
    @Operation(summary = "修改角色")
    public boolean update(@RequestBody Role role) {
        return roleService.save(role);
    }

    @PostMapping("/remove/{id}")
    @Operation(summary = "删除角色（逻辑删除）")
    public boolean remove(@PathVariable Long id) {
        return roleService.removeById(id);
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "物理删除角色")
    public boolean delete(@PathVariable Long id) {
        return roleService.deleteById(id);
    }
}
