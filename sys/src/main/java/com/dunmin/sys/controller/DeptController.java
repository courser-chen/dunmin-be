package com.dunmin.sys.controller;

import com.dunmin.model.dto.PageDTO;
import com.dunmin.model.entity.sys.Dept;
import com.dunmin.sys.service.DeptService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门 Controller
 */
@RestController
@RequestMapping("/dept")
@Tag(name = "部门管理")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping("/{id}")
    @Operation(summary = "获取部门详情")
    public Dept getById(@PathVariable Long id) {
        return deptService.getById(id);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询部门")
    public Page<Dept> page(PageDTO pageDTO) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .from(Dept.class);

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

        return deptService.page(pageDTO.getPage(), pageDTO.getPageSize(), queryWrapper);
    }

    @GetMapping("/children")
    @Operation(summary = "查询子部门")
    public List<Dept> listByParentId(@RequestParam Long parentId) {
        return deptService.listByParentId(parentId);
    }

    @GetMapping("/list")
    @Operation(summary = "根据组织查询部门")
    public List<Dept> listByOrgId(@RequestParam Long orgId) {
        return deptService.listByOrgId(orgId);
    }

    @GetMapping("/list/all")
    @Operation(summary = "查询所有部门")
    public List<Dept> listAll() {
        return deptService.listAll();
    }

    @PostMapping
    @Operation(summary = "新增部门")
    public boolean save(@RequestBody Dept dept) {
        if (dept.getId() == null) {
            // 新增
        }
        return deptService.save(dept);
    }

    @PostMapping("/update")
    @Operation(summary = "修改部门")
    public boolean update(@RequestBody Dept dept) {
        return deptService.save(dept);
    }

    @PostMapping("/remove/{id}")
    @Operation(summary = "删除部门（逻辑删除）")
    public boolean remove(@PathVariable Long id) {
        return deptService.removeById(id);
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "物理删除部门")
    public boolean delete(@PathVariable Long id) {
        return deptService.deleteById(id);
    }
}
