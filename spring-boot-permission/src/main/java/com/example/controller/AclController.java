package com.example.controller;

import com.example.entity.SysRole;
import com.example.req.AclModuleReq;
import com.example.req.AclReq;
import com.example.service.SysAclService;
import com.example.service.SysRoleService;
import com.example.utils.ResultVoUtil;
import com.example.vo.ResVo;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2023/10/23
 * @Classname AclController
 * @since 1.0.0
 */
@RestController
@RequestMapping("/acl")
@Tag(name = "权限控制类")
public class AclController {
    @Resource
    private SysAclService sysAclService;
    @Resource
    private SysRoleService sysRoleService;

    @PostMapping("/save")
    @Operation(summary = "权限点新增",description = "新增")
    public ResVo save(@Validated @RequestBody AclReq aclReq) {
        sysAclService.saveAcl(aclReq);
        return ResultVoUtil.success();
    }

    @PostMapping("/update")
    @Operation(summary = "权限点更新",description = "更新")
    public ResVo update(@Validated @RequestBody AclReq aclReq) {
        sysAclService.updateAcl(aclReq);
        return ResultVoUtil.success();
    }

    @PostMapping("/delete")
    @Operation(summary = "权限点删除",description = "删除")
    @Parameters({
            @Parameter(name = "id",example = "1",description = "权限点id",required = true,in = ParameterIn.QUERY),
    })
    public ResVo delete(@RequestParam("id") int id) {
        sysAclService.deleteAcl(id);
        return ResultVoUtil.success();
    }
    @RequestMapping("acls.json")
    @ResponseBody
    public ResVo acls(@RequestParam("aclId") int aclId) {
        Map<String, Object> map = Maps.newHashMap();
//        List<SysRole> roleList = sysRoleService.getRoleListByAclId(aclId);
//        map.put("roles", roleList);
//        map.put("users", sysRoleService.getUserListByRoleList(roleList));
        return ResultVoUtil.success();
    }
}
