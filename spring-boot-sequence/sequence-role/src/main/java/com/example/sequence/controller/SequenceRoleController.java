package com.example.sequence.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.example.common.model.PageConfig;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.example.common.model.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import io.swagger.annotations.Api;
import com.example.sequence.api.model.vo.SequenceRoleVO;
import com.example.sequence.api.model.dto.SequenceRoleDTO;
import lombok.RequiredArgsConstructor;

import com.example.sequence.service.SequenceRoleService;

/**
 * <p>
 * 流水号规则表 前端控制器
 * </p>
 *
 * @author wgs
 * @since 2024-09-29
 */
@Slf4j
@RestController
@RequestMapping("/sequence-role")
@Valid
@RequiredArgsConstructor
@Api(tags = {"流水号规则表管理"})
public class SequenceRoleController {

    private final SequenceRoleService sequenceRoleService;

    @ApiOperation(value = "流水号规则表分页查询", httpMethod = "POST", response = SequenceRoleVO.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "current", value = "当前页", dataType = "int", example = "1"),
        @ApiImplicitParam(name = "size", value = "分页显示数量", dataType = "int", example = "10"),
    })
    @PostMapping("/page")
    public Result<Page<SequenceRoleVO>> insidePageSequenceRole(@RequestParam(value="current") Integer current,@RequestParam(value="size") Integer size,
                                                    @RequestBody SequenceRoleDTO sequenceRoleDto) {
        Integer currentPage = PageConfig.currentPagination(current);
        Integer pageSize = PageConfig.pageSize(size);
        return Result.buildOkData(sequenceRoleService.insidePageEnhance(new Page<>(currentPage, pageSize), sequenceRoleDto));
    }

    @ApiOperation(value = "流水号规则表列表查询", httpMethod = "POST", response = SequenceRoleVO.class)
    @PostMapping("/list")
    public Result<List<SequenceRoleVO>> insideListSequenceRole(@RequestBody SequenceRoleDTO sequenceRoleDto) {
        return Result.buildOkData(sequenceRoleService.insideListEnhance(sequenceRoleDto));
    }

    @ApiOperation(value = "流水号规则表查询详情", httpMethod = "GET", response = SequenceRoleVO.class)
    @GetMapping("/getById")
    public Result<SequenceRoleVO> insideGetSequenceRole(@RequestParam(value="id") Long id) {
        return Result.buildOkData(sequenceRoleService.insideGetEnhanceById(id));
    }

    @ApiOperation(value = "条件查询流水号规则表详情", httpMethod = "GET", response = SequenceRoleVO.class)
    @GetMapping("/getByParam")
    public Result<SequenceRoleVO> insideGetOneByParam(@Validated SequenceRoleDTO sequenceRoleDto) {
        return Result.buildOkData(sequenceRoleService.insideGetOneEnhanceByParam(sequenceRoleDto));
    }

    @ApiOperation(value = "流水号规则表新增", httpMethod = "POST", response = Boolean.class)
    @PostMapping("/save")
    public Result<Long> insideSave(@RequestBody SequenceRoleDTO sequenceRoleDto) {
        return Result.buildOkData(sequenceRoleService.insideSaveEnhance(sequenceRoleDto));
    }

    @ApiOperation(value = "流水号规则表修改", httpMethod = "POST", response = Boolean.class)
    @PostMapping("/update")
    public Result<Long> insideUpdate(@RequestBody SequenceRoleDTO sequenceRoleDto) {
        return Result.buildOkData(sequenceRoleService.insideUpdateEnhance(sequenceRoleDto));
    }

    @ApiOperation(value = "流水号规则表删除", httpMethod = "DELETE", response = Boolean.class)
    @DeleteMapping("/delete")
    public Result<Boolean> insideRemoveById(@RequestParam(value="id") Long id) {
        return Result.buildOkData(sequenceRoleService.insideRemoveEnhanceById(id));
    }

}
