package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.example.dto.AclModuleLevelDto;
import com.example.req.RoleAclReq;
import com.example.req.RoleReq;
import com.example.req.RoleUserReq;
import com.example.service.SysRoleAclService;
import com.example.service.SysRoleService;
import com.example.service.SysRoleUserService;
import com.example.service.SysTreeService;
import com.example.utils.ResultVoUtil;
import com.example.vo.ResVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: wgs
 * @Date 2023/10/20
 * @Classname RoleControler
 * @since 1.0.0
 */
@RestController
@RequestMapping("/role")
@Tag(name = "角色控制类")
public class RoleController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private SysRoleAclService sysRoleAclService;
    @Autowired
    private SysTreeService sysTreeService;

    @PostMapping("/save")
    @Operation(summary = "新增角色", description = "新增")
    public ResVo save(@Validated @RequestBody RoleReq roleReq) {
        sysRoleService.saveRole(roleReq);
        return ResultVoUtil.success();
    }

    @PostMapping("/update")
    @Operation(summary = "更新角色", description = "新增")
    public ResVo update(@Validated @RequestBody RoleReq roleReq) {
        sysRoleService.updateRole(roleReq);
        return ResultVoUtil.success();
    }

    @GetMapping("/list")
    @Operation(summary = "查询所有角色", description = "")
    public ResVo queryAll() {
        return ResultVoUtil.success(sysRoleService.list());
    }

    @GetMapping("/roleTree")
    @Parameters(@Parameter(name = "roleId", example = "1", description = "角色id", required = true, in = ParameterIn.QUERY))
    public ResVo roleTree(@RequestParam("roleId") int roleId) {

        List<AclModuleLevelDto> roleTree  = sysTreeService.roleTree(roleId);
        return ResultVoUtil.success();
    }

    @Operation(summary = "角色与权限绑定", description = "")
    @PostMapping("/changeAcls.json")
    public ResVo changeAcls(@Validated @RequestBody RoleAclReq roleAclReq) {
        sysRoleAclService.changeRoleAcls(roleAclReq);
        return ResultVoUtil.success();
    }

    @PostMapping("/changeUsers.json")
    @Operation(summary = "角色与用户绑定", description = "")
    public ResVo changeUsers(@Validated @RequestBody RoleUserReq roleUserReq) {
        sysRoleUserService.changeRoleUsers(roleUserReq);
        return ResultVoUtil.success();
    }

    @GetMapping("/users")
    @Parameters(@Parameter(name = "roleId", example = "1", description = "角色id", required = true, in = ParameterIn.QUERY))
    @Operation(summary = "查询选择与未选中用户", description = "")
    public ResVo users(@RequestParam("roleId") int roleId) {
        JSON json = sysRoleUserService.querySelectedAandUnselectedByRoleId(roleId);
        return ResultVoUtil.success(json);
    }

}
