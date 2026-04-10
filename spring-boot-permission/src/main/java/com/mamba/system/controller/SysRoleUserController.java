package com.mamba.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.mamba.common.base.BaseController;
import com.mamba.common.result.R;
import com.mamba.system.dto.RoleUserDTO;
import com.mamba.system.service.SysRoleUserService;
import com.mamba.system.service.SysUserService;
import com.mamba.system.vo.SysUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 角色用户分配
 */
@Slf4j
@RestController
@RequestMapping("/api/system/roleUser")
@Api(tags = "角色用户分配")
public class SysRoleUserController extends BaseController {

    @Resource
    private SysRoleUserService sysRoleUserService;

    @Resource
    private SysUserService sysUserService;

    /**
     * 查询角色下的用户ID列表
     */
    @GetMapping("/role/{roleId}")
    @ApiOperation(value = "查询角色下的用户ID列表")
    @SaCheckLogin
    public R<List<Integer>> getUserIdsByRoleId(@PathVariable Integer roleId) {
        List<Integer> userIds = sysRoleUserService.getUserIdsByRoleId(roleId);
        log.info("角色ID: {}, 绑定的用户ID列表: {}", roleId, userIds);
        return R.ok(userIds);
    }

    /**
     * 查询用户下的角色ID列表
     */
    @GetMapping("/user/{userId}")
    @ApiOperation(value = "查询用户下的角色ID列表")
    public R<List<Integer>> getRoleIdsByUserId(@PathVariable Integer userId) {
        return R.ok(sysRoleUserService.getRoleIdsByUserId(userId));
    }

    /**
     * 角色分配用户
     */
    @PostMapping
    @ApiOperation(value = "角色分配用户")
    @SaCheckLogin
    public R<Void> assignUsers(@RequestBody @Valid RoleUserDTO dto) {
        sysRoleUserService.assignUsers(dto);
        return R.ok();
    }

    /**
     * 查询角色下的用户列表
     */
    @GetMapping("/role/{roleId}/users")
    @ApiOperation(value = "查询角色下的用户列表")
    public R<List<SysUserVO>> listUsersByRoleId(@PathVariable Integer roleId) {
        return R.ok(sysUserService.listByRoleId(roleId));
    }
}
