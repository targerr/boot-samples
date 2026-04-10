package com.mamba.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.mamba.common.base.BaseController;
import com.mamba.common.result.PageResult;
import com.mamba.common.result.R;
import com.mamba.system.dto.PageQueryDTO;
import com.mamba.system.dto.SysRoleDTO;
import com.mamba.system.entity.SysRole;
import com.mamba.system.service.SysRoleService;
import com.mamba.system.vo.SysRoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 角色管理
 */
@Slf4j
@RestController
@RequestMapping("/api/system/role")
@Api(tags = "角色管理")
public class SysRoleController extends BaseController {

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 分页查询角色
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询角色")
    public R<PageResult<SysRoleVO>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        PageQueryDTO queryDTO = new PageQueryDTO();
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);
        queryDTO.setKeyword(keyword);
        return R.ok(sysRoleService.page(queryDTO));
    }

    /**
     * 查询所有角色
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询所有角色")
    public R<List<SysRoleVO>> listAll() {
        return R.ok(sysRoleService.listAll());
    }

    /**
     * 查询角色详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询角色详情")
    public R<SysRoleVO> getById(@PathVariable Integer id) {
        SysRole byId = sysRoleService.getById(id);
        SysRoleVO vo = new SysRoleVO();
        BeanUtils.copyProperties(byId, vo);
        return R.ok(vo);
    }

    /**
     * 新增角色
     */
    @PostMapping
    @ApiOperation(value = "新增角色")
    @SaCheckLogin
    public R<Void> save(@RequestBody @Valid SysRoleDTO dto) {
        sysRoleService.saveRole(dto);
        return R.ok();
    }

    /**
     * 更新角色
     */
    @PutMapping
    @ApiOperation(value = "更新角色")
    @SaCheckLogin
    public R<Void> update(@RequestBody @Valid SysRoleDTO dto) {
        sysRoleService.updateRole(dto);
        return R.ok();
    }

    /**
     * 切换角色状态
     */
    @PutMapping("/changeStatus")
    @ApiOperation(value = "切换角色状态")
    @SaCheckLogin
    public R<Void> changeStatus(@RequestParam Integer id, @RequestParam Integer status) {
        sysRoleService.changeStatus(id, status);
        return R.ok();
    }
}
