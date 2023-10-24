package com.example.controller;

import com.example.dto.UserInfoDTO;
import com.example.req.UserReq;
import com.example.service.SysRoleService;
import com.example.service.SysTreeService;
import com.example.service.SysUserService;
import com.example.utils.ResultVoUtil;
import com.example.vo.ResVo;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: wgs
 * @Date 2023/10/17 15:05
 * @Classname UserController
 * @Description
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户控制器")
public class SysUserController {
    @Autowired
    private SysTreeService sysTreeService;
    @Autowired
    private SysRoleService sysRoleService;

    @Operation(summary = "获取用户权限与角色",description = "获取用户权限与角色")
    @Parameters({
            @Parameter(name = "userId",example = "1",description = "用户id",required = true,in = ParameterIn.QUERY),
    })
    @RequestMapping("/acls")
    public ResVo acls(@RequestParam("userId") int userId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("acls", sysTreeService.userAclTree(userId));
        map.put("roles", sysRoleService.getRoleListByUserId(userId));
        return ResultVoUtil.success(map);
    }

}
