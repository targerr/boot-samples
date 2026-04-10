package com.mamba.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.mamba.common.base.BaseController;
import com.mamba.common.result.R;
import com.mamba.system.dto.LoginDTO;
import com.mamba.system.service.SysUserService;
import com.mamba.system.vo.SysUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证管理
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@Api(tags = "认证管理")
public class AuthController extends BaseController {

    @Resource
    private SysUserService sysUserService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    public R<Map<String, Object>> login(@RequestBody @Valid LoginDTO dto) {
        SysUserVO userVO = sysUserService.login(dto.getUsername(), dto.getPassword());
        Map<String, Object> result = new HashMap<>();
        result.put("token", StpUtil.getTokenValue());
        result.put("user", userVO);
        return R.ok(result);
    }

    /**
     * 用户退出
     */
    @PostMapping("/logout")
    @ApiOperation(value = "用户退出")
    @SaCheckLogin
    public R<Void> logout() {
        StpUtil.logout();
        return R.ok();
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/userInfo")
    @ApiOperation(value = "获取当前用户信息")
    @SaCheckLogin
    public R<SysUserVO> getUserInfo() {
        return R.ok(sysUserService.getUserInfo());
    }

    /**
     * 获取当前用户权限列表（菜单权限）
     */
    @GetMapping("/userPermissions")
    @ApiOperation(value = "获取当前用户权限列表")
    @SaCheckLogin
    public R<java.util.Map<String, Object>> getUserPermissions() {
        return R.ok(sysUserService.getUserPermissions());
    }
}
