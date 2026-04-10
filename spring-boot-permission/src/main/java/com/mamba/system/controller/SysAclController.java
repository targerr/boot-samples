package com.mamba.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.mamba.common.base.BaseController;
import com.mamba.common.result.PageResult;
import com.mamba.common.result.R;
import com.mamba.system.dto.PageQueryDTO;
import com.mamba.system.dto.SysAclDTO;
import com.mamba.system.service.SysAclService;
import com.mamba.system.vo.SysAclVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 权限点管理
 */
@Slf4j
@RestController
@RequestMapping("/api/system/acl")
@Api(tags = "权限点管理")
public class SysAclController extends BaseController {

    @Resource
    private SysAclService sysAclService;

    /**
     * 分页查询权限点
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询权限点")
    public R<PageResult<SysAclVO>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        PageQueryDTO queryDTO = new PageQueryDTO();
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);
        queryDTO.setKeyword(keyword);
        queryDTO.setStatus(status);
        return R.ok(sysAclService.page(queryDTO));
    }

    /**
     * 查询模块下权限点
     */
    @GetMapping("/module/{moduleId}")
    @ApiOperation(value = "查询模块下权限点")
    public R<List<SysAclVO>> listByModuleId(@PathVariable Integer moduleId) {
        return R.ok(sysAclService.listByModuleId(moduleId));
    }

    /**
     * 查询角色权限点(含选中状态)
     */
    @GetMapping("/role/{roleId}")
    @ApiOperation(value = "查询角色权限点(含选中状态)")
    public R<List<SysAclVO>> listByRole(@PathVariable Integer roleId) {
        return R.ok(sysAclService.listByRole(roleId));
    }

    /**
     * 新增权限点
     */
    @PostMapping
    @ApiOperation(value = "新增权限点")
    @SaCheckLogin
    public R<Void> save(@RequestBody @Valid SysAclDTO dto) {
        sysAclService.saveAcl(dto);
        return R.ok();
    }

    /**
     * 更新权限点
     */
    @PutMapping
    @ApiOperation(value = "更新权限点")
    @SaCheckLogin
    public R<Void> update(@RequestBody @Valid SysAclDTO dto) {
        sysAclService.updateAcl(dto);
        return R.ok();
    }

    /**
     * 切换权限点状态
     */
    @PutMapping("/changeStatus")
    @ApiOperation(value = "切换权限点状态")
    @SaCheckLogin
    public R<Void> changeStatus(@RequestParam Integer id, @RequestParam Integer status) {
        sysAclService.changeStatus(id, status);
        return R.ok();
    }
}
