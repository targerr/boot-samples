package com.example.sequence.controller;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.enums.EncryptField;
import com.example.sequence.api.model.dto.SequenceRoleDTO;
import com.example.sequence.api.model.vo.SequenceRoleVO;
import com.example.sequence.service.SequenceRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @ApiOperation(value = "流水号规则表列表查询", httpMethod = "POST", response = SequenceRoleVO.class)
    @PostMapping("/list")
    public List<SequenceRoleVO> insideListSequenceRole(@RequestBody SequenceRoleDTO sequenceRoleDto) {
        return sequenceRoleService.insideListEnhance(sequenceRoleDto);
    }

    @ApiOperation(value = "流水号规则表查询详情", httpMethod = "GET", response = SequenceRoleVO.class)
    @GetMapping("/getById")
    public SequenceRoleVO insideGetSequenceRole(@RequestParam(value="id") Long id) {
        return sequenceRoleService.insideGetEnhanceById(id);
    }

    @ApiOperation(value = "条件查询流水号规则表详情", httpMethod = "GET", response = SequenceRoleVO.class)
    @GetMapping("/getByParam")
    public SequenceRoleVO insideGetOneByParam(@Validated SequenceRoleDTO sequenceRoleDto) {
        return sequenceRoleService.insideGetOneEnhanceByParam(sequenceRoleDto);
    }

    @ApiOperation(value = "流水号规则表新增", httpMethod = "POST", response = Boolean.class)
    @PostMapping("/save")
    @EncryptField(keys = {"sequenceName"})
    public Long insideSave(@RequestBody SequenceRoleDTO sequenceRoleDto) {
        return sequenceRoleService.insideSaveEnhance(sequenceRoleDto);
    }

}
