package com.mamba.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mamba.common.base.BaseController;
import com.mamba.common.result.PageResult;
import com.mamba.common.result.R;
import com.mamba.system.dto.PageQueryDTO;
import com.mamba.system.dto.SysUserDTO;
import com.mamba.system.entity.SysUser;
import com.mamba.system.service.SysUserService;
import com.mamba.system.vo.SysUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 用户管理
 */
@Slf4j
@RestController
@RequestMapping("/api/system/user")
@Api(tags = "用户管理")
public class SysUserController extends BaseController {

    @Resource
    private SysUserService sysUserService;

    /**
     * 分页查询用户
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询用户")
    public R<PageResult<SysUserVO>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        PageQueryDTO queryDTO = new PageQueryDTO();
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);
        queryDTO.setKeyword(keyword);
        queryDTO.setStatus(status);
        return R.ok(sysUserService.page(queryDTO));
    }

    /**
     * 查询用户详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询用户详情")
    public R<SysUserVO> getById(@PathVariable Integer id) {
        SysUser byId = sysUserService.getById(id);
        SysUserVO vo = new SysUserVO();
        BeanUtils.copyProperties(byId, vo);
        return R.ok(vo);
    }

    /**
     * 新增用户
     */
    @PostMapping
    @ApiOperation(value = "新增用户")
    @SaCheckPermission("USER_ADD")
    public R<Void> save(@RequestBody @Valid SysUserDTO dto) {
        sysUserService.saveUser(dto);
        return R.ok();
    }

    /**
     * 更新用户
     */
    @PutMapping
    @ApiOperation(value = "更新用户")
    @SaCheckPermission("USER_EDIT")
    public R<Void> update(@RequestBody @Valid SysUserDTO dto) {
        sysUserService.updateUser(dto);
        return R.ok();
    }

    /**
     * 切换用户状态
     */
    @PutMapping("/changeStatus")
    @ApiOperation(value = "切换用户状态")
    @SaCheckPermission("USER_STATUS")
    public R<Void> changeStatus(@RequestParam Integer id, @RequestParam Integer status) {
        sysUserService.changeStatus(id, status);
        return R.ok();
    }

    /**
     * 批量删除用户
     */
    @DeleteMapping
    @ApiOperation(value = "批量删除用户")
    @SaCheckPermission("USER_DELETE")
    public R<Void> deleteUsers(@RequestBody List<Integer> ids) {
        sysUserService.deleteUsers(ids);
        return R.ok();
    }
}
