package com.mamba.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.mamba.common.base.BaseController;
import com.mamba.common.result.R;
import com.mamba.system.dto.SysDeptDTO;
import com.mamba.system.entity.SysDept;
import com.mamba.system.service.SysDeptService;
import com.mamba.system.vo.SysDeptVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 部门管理
 */
@Slf4j
@RestController
@RequestMapping("/api/system/dept")
@Api(tags = "部门管理")
public class SysDeptController extends BaseController {

    @Resource
    private SysDeptService sysDeptService;

    /**
     * 获取部门树
     */
    @GetMapping("/tree")
    @ApiOperation(value = "获取部门树")
    public R<List<SysDeptVO>> tree() {
        return R.ok(sysDeptService.tree());
    }

    /**
     * 查询部门详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询部门详情")
    public R<SysDeptVO> getById(@PathVariable Integer id) {
        SysDept byId = sysDeptService.getById(id);
        SysDeptVO vo = new SysDeptVO();
        BeanUtils.copyProperties(byId, vo);
        return R.ok(vo);
    }

    /**
     * 新增部门
     */
    @PostMapping
    @ApiOperation(value = "新增部门")
    @SaCheckLogin
    public R<Void> save(@RequestBody @Valid SysDeptDTO dto) {
        sysDeptService.saveDept(dto);
        return R.ok();
    }

    /**
     * 更新部门
     */
    @PutMapping
    @ApiOperation(value = "更新部门")
    @SaCheckLogin
    public R<Void> update(@RequestBody @Valid SysDeptDTO dto) {
        sysDeptService.updateDept(dto);
        return R.ok();
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除部门")
    @SaCheckLogin
    public R<Void> delete(@PathVariable Integer id) {
        sysDeptService.deleteDept(id);
        return R.ok();
    }
}
