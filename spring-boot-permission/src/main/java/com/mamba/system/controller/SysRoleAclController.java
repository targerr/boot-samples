package com.mamba.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.mamba.common.base.BaseController;
import com.mamba.common.result.R;
import com.mamba.system.dto.RoleAclDTO;
import com.mamba.system.service.SysRoleAclService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 角色权限分配
 */
@Slf4j
@RestController
@RequestMapping("/api/system/roleAcl")
@Api(tags = "角色权限分配")
public class SysRoleAclController extends BaseController {

    @Resource
    private SysRoleAclService sysRoleAclService;

    /**
     * 查询角色权限ID列表
     */
    @GetMapping("/{roleId}")
    @ApiOperation(value = "查询角色权限ID列表")
    public R<List<Integer>> getAclIdsByRoleId(@PathVariable Integer roleId) {
        return R.ok(sysRoleAclService.getAclIdsByRoleId(roleId));
    }

    /**
     * 角色分配权限
     */
    @PostMapping
    @ApiOperation(value = "角色分配权限")
    @SaCheckLogin
    public R<Void> assignAcls(@RequestBody @Valid RoleAclDTO dto) {
        sysRoleAclService.assignAcls(dto);
        return R.ok();
    }
}
