package com.mamba.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.mamba.common.base.BaseController;
import com.mamba.common.result.R;
import com.mamba.system.dto.SysAclModuleDTO;
import com.mamba.system.service.SysAclModuleService;
import com.mamba.system.vo.SysAclModuleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 权限模块管理
 */
@Slf4j
@RestController
@RequestMapping("/api/system/aclModule")
@Api(tags = "权限模块管理")
public class SysAclModuleController extends BaseController {

    @Resource
    private SysAclModuleService sysAclModuleService;

    /**
     * 获取权限模块树(含权限点)
     */
    @GetMapping("/tree")
    @ApiOperation(value ="获取权限模块树(含权限点)")
    public R<List<SysAclModuleVO>> treeWithAcls() {
        return R.ok(sysAclModuleService.treeWithAcls());
    }

    /**
     * 新增权限模块
     */
    @PostMapping
    @ApiOperation(value ="新增权限模块")
    @SaCheckLogin
    public R<Void> save(@RequestBody @Valid SysAclModuleDTO dto) {
        sysAclModuleService.saveModule(dto);
        return R.ok();
    }

    /**
     * 更新权限模块
     */
    @PutMapping
    @ApiOperation(value ="更新权限模块")
    @SaCheckLogin
    public R<Void> update(@RequestBody @Valid SysAclModuleDTO dto) {
        sysAclModuleService.updateModule(dto);
        return R.ok();
    }

    /**
     * 切换权限模块状态
     */
    @PutMapping("/changeStatus")
    @ApiOperation(value ="切换权限模块状态")
    @SaCheckLogin
    public R<Void> changeStatus(@RequestParam Integer id, @RequestParam Integer status) {
        sysAclModuleService.changeStatus(id, status);
        return R.ok();
    }

    /**
     * 删除权限模块
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value ="删除权限模块")
    @SaCheckLogin
    public R<Void> delete(@PathVariable Integer id) {
        sysAclModuleService.deleteModule(id);
        return R.ok();
    }
}
