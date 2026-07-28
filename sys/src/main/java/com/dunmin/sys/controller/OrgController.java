package com.dunmin.sys.controller;

import com.dunmin.model.dto.PageDTO;
import com.dunmin.model.entity.sys.Org;
import com.dunmin.sys.service.OrgService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 组织 Controller
 */
@RestController
@RequestMapping("/org")
@Tag(name = "组织管理")
public class OrgController {

    @Autowired
    private OrgService orgService;

    @GetMapping("/{id}")
    @Operation(summary = "获取组织详情")
    public Org getById(@PathVariable Long id) {
        return orgService.getById(id);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询组织")
    public Page<Org> page(PageDTO pageDTO) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .from(Org.class);

        if (pageDTO.getKeyword() != null && !pageDTO.getKeyword().isEmpty()) {
            queryWrapper.and("name like ? or code like ?",
                    "%" + pageDTO.getKeyword() + "%",
                    "%" + pageDTO.getKeyword() + "%");
        }

        if (pageDTO.getOrderBy() != null && !pageDTO.getOrderBy().isEmpty()) {
            queryWrapper.orderBy(pageDTO.getOrderBy(), pageDTO.isAsc());
        } else {
            queryWrapper.orderBy("id desc");
        }

        return orgService.page(pageDTO.getPage(), pageDTO.getPageSize(), queryWrapper);
    }

    @GetMapping("/children")
    @Operation(summary = "查询子组织")
    public List<Org> listByParentId(@RequestParam Long parentId) {
        return orgService.listByParentId(parentId);
    }

    @GetMapping("/list/all")
    @Operation(summary = "查询所有组织")
    public List<Org> listAll() {
        return orgService.listAll();
    }

    @PostMapping
    @Operation(summary = "新增组织")
    public boolean save(@RequestBody Org org) {
        if (org.getId() == null) {
            // 新增
        }
        return orgService.save(org);
    }

    @PostMapping("/update")
    @Operation(summary = "修改组织")
    public boolean update(@RequestBody Org org) {
        return orgService.save(org);
    }

    @PostMapping("/remove/{id}")
    @Operation(summary = "删除组织（逻辑删除）")
    public boolean remove(@PathVariable Long id) {
        return orgService.removeById(id);
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "物理删除组织")
    public boolean delete(@PathVariable Long id) {
        return orgService.deleteById(id);
    }
}
