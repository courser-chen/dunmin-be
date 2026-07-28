package com.dunmin.cfg.controller;

import com.dunmin.cfg.service.FormService;
import com.dunmin.model.dto.PageDTO;
import com.dunmin.model.entity.config.Form;
import com.dunmin.model.entity.config.FormField;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 表单 Controller
 */
@RestController
@RequestMapping("/form")
@Tag(name = "表单管理")
public class FormController {

    @Autowired
    private FormService formService;

    // ==================== Form 接口 ====================

    @GetMapping("/{id}")
    @Operation(summary = "获取表单详情")
    public Form getById(@PathVariable Long id) {
        return formService.getById(id);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "根据编码查询")
    public Form getByCode(@PathVariable String code) {
        return formService.getByCode(code);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询表单")
    public Page<Form> page(PageDTO pageDTO) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .from(Form.class);

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

        return formService.page(pageDTO.getPage(), pageDTO.getPageSize(), pageDTO.getKeyword());
    }

    @GetMapping("/list/all")
    @Operation(summary = "查询所有表单")
    public List<Form> listAll() {
        return formService.listAll();
    }

    @PostMapping
    @Operation(summary = "新增表单")
    public boolean save(@RequestBody Form form) {
        if (form.getId() == null) {
            // 新增
            if (formService.existsByCode(form.getCode())) {
                throw new RuntimeException("表单编码已存在");
            }
        }
        return formService.save(form);
    }

    @PostMapping("/update")
    @Operation(summary = "修改表单")
    public boolean update(@RequestBody Form form) {
        return formService.save(form);
    }

    @PostMapping("/remove/{id}")
    @Operation(summary = "删除表单（逻辑删除）")
    public boolean remove(@PathVariable Long id) {
        formService.removeFieldsByFormId(id);
        return formService.removeById(id);
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "物理删除表单")
    public boolean delete(@PathVariable Long id) {
        formService.removeFieldsByFormId(id);
        return formService.deleteById(id);
    }

    // ==================== FormField 接口 ====================

    @GetMapping("/{formId}/field")
    @Operation(summary = "获取表单的所有字段")
    public List<FormField> listFields(@PathVariable Long formId) {
        return formService.listFields(formId);
    }

    @GetMapping("/field/{id}")
    @Operation(summary = "获取字段详情")
    public FormField getField(@PathVariable Long id) {
        return formService.getField(id);
    }

    @PostMapping("/field/save")
    @Operation(summary = "保存字段（新增或修改）")
    public boolean saveField(@RequestBody FormField field) {
        return formService.saveField(field);
    }

    @PostMapping("/field/remove/{id}")
    @Operation(summary = "删除字段（逻辑删除）")
    public boolean removeField(@PathVariable Long id) {
        return formService.removeField(id);
    }

    @PostMapping("/field/delete/{id}")
    @Operation(summary = "物理删除字段")
    public boolean deleteField(@PathVariable Long id) {
        return formService.deleteField(id);
    }
}
