package com.dunmin.sys.controller;

import com.dunmin.model.dto.PageDTO;
import com.dunmin.model.entity.sys.User;
import com.dunmin.sys.service.UserService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户 Controller
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名查询")
    public User getByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询用户")
    public Page<User> page(PageDTO pageDTO) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .from(User.class);

        if (pageDTO.getKeyword() != null && !pageDTO.getKeyword().isEmpty()) {
            queryWrapper.and("username like ? or nickname like ? or phone like ? or email like ?",
                    "%" + pageDTO.getKeyword() + "%",
                    "%" + pageDTO.getKeyword() + "%",
                    "%" + pageDTO.getKeyword() + "%",
                    "%" + pageDTO.getKeyword() + "%");
        }

        if (pageDTO.getOrderBy() != null && !pageDTO.getOrderBy().isEmpty()) {
            queryWrapper.orderBy(pageDTO.getOrderBy(), pageDTO.isAsc());
        } else {
            queryWrapper.orderBy("id desc");
        }

        return userService.page(pageDTO.getPage(), pageDTO.getPageSize(), queryWrapper);
    }

    @GetMapping("/list")
    @Operation(summary = "根据部门查询用户")
    public List<User> listByDeptId(@RequestParam Long deptId) {
        return userService.listByDeptId(deptId);
    }

    @GetMapping("/list/all")
    @Operation(summary = "查询所有用户")
    public List<User> listAll() {
        return userService.listAll();
    }

    @PostMapping
    @Operation(summary = "新增用户")
    public boolean save(@RequestBody User user) {
        if (user.getId() == null) {
            if (userService.existsByUsername(user.getUsername())) {
                throw new RuntimeException("用户名已存在");
            }
        }
        return userService.save(user);
    }

    @PostMapping("/update")
    @Operation(summary = "修改用户")
    public boolean update(@RequestBody User user) {
        return userService.save(user);
    }

    @PostMapping("/remove/{id}")
    @Operation(summary = "删除用户（逻辑删除）")
    public boolean remove(@PathVariable Long id) {
        return userService.removeById(id);
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "物理删除用户")
    public boolean delete(@PathVariable Long id) {
        return userService.deleteById(id);
    }
}
