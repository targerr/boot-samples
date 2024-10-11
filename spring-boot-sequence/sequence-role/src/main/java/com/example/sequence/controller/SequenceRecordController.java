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
import com.example.sequence.api.model.vo.SequenceRecordVO;
import com.example.sequence.api.model.dto.SequenceRecordDTO;
import lombok.RequiredArgsConstructor;

import com.example.sequence.service.SequenceRecordService;

/**
 * <p>
 * 流水号表 前端控制器
 * </p>
 *
 * @author wgs
 * @since 2024-09-29
 */
@Slf4j
@RestController
@RequestMapping("/sequence-record")
@Valid
@RequiredArgsConstructor
@Api(tags = {"流水号表管理"})
public class SequenceRecordController {

    private final SequenceRecordService sequenceRecordService;

    @ApiOperation(value = "流水号表分页查询", httpMethod = "POST", response = SequenceRecordVO.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "current", value = "当前页", dataType = "int", example = "1"),
        @ApiImplicitParam(name = "size", value = "分页显示数量", dataType = "int", example = "10"),
    })
    @PostMapping("/page")
    public Result<Page<SequenceRecordVO>> insidePageSequenceRecord(@RequestParam(value="current") Integer current,@RequestParam(value="size") Integer size,
                                                    @RequestBody SequenceRecordDTO sequenceRecordDto) {
        Integer currentPage = PageConfig.currentPagination(current);
        Integer pageSize = PageConfig.pageSize(size);
        return Result.buildOkData(sequenceRecordService.insidePageEnhance(new Page<>(currentPage, pageSize), sequenceRecordDto));
    }

    @ApiOperation(value = "流水号表列表查询", httpMethod = "POST", response = SequenceRecordVO.class)
    @PostMapping("/list")
    public Result<List<SequenceRecordVO>> insideListSequenceRecord(@RequestBody SequenceRecordDTO sequenceRecordDto) {
        return Result.buildOkData(sequenceRecordService.insideListEnhance(sequenceRecordDto));
    }

    @ApiOperation(value = "流水号表查询详情", httpMethod = "GET", response = SequenceRecordVO.class)
    @GetMapping("/getById")
    public Result<SequenceRecordVO> insideGetSequenceRecord(@RequestParam(value="id") Long id) {
        return Result.buildOkData(sequenceRecordService.insideGetEnhanceById(id));
    }

    @ApiOperation(value = "条件查询流水号表详情", httpMethod = "GET", response = SequenceRecordVO.class)
    @GetMapping("/getByParam")
    public Result<SequenceRecordVO> insideGetOneByParam(@Validated SequenceRecordDTO sequenceRecordDto) {
        return Result.buildOkData(sequenceRecordService.insideGetOneEnhanceByParam(sequenceRecordDto));
    }

    @ApiOperation(value = "流水号表新增", httpMethod = "POST", response = Boolean.class)
    @PostMapping("/save")
    public Result<Long> insideSave(@RequestBody SequenceRecordDTO sequenceRecordDto) {
        return Result.buildOkData(sequenceRecordService.insideSaveEnhance(sequenceRecordDto));
    }

    @ApiOperation(value = "流水号表修改", httpMethod = "POST", response = Boolean.class)
    @PostMapping("/update")
    public Result<Long> insideUpdate(@RequestBody SequenceRecordDTO sequenceRecordDto) {
        return Result.buildOkData(sequenceRecordService.insideUpdateEnhance(sequenceRecordDto));
    }

    @ApiOperation(value = "流水号表删除", httpMethod = "DELETE", response = Boolean.class)
    @DeleteMapping("/delete")
    public Result<Boolean> insideRemoveById(@RequestParam(value="id") Long id) {
        return Result.buildOkData(sequenceRecordService.insideRemoveEnhanceById(id));
    }

}
