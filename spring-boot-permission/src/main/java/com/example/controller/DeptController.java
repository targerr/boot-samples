package com.example.controller;

import com.example.dto.UserInfoDTO;
import com.example.req.DeptReq;
import com.example.service.SysDeptService;
import com.example.service.SysTreeService;
import com.example.utils.ResultVoUtil;
import com.example.vo.DeptVo;
import com.example.vo.ResVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: wgs
 * @Date 2023/10/19 14:35
 * @Classname DeptController
 * @Description
 */
@RestController
@RequestMapping("/dept")
@Tag(name = "部门控制器")
public class DeptController {
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysTreeService sysTreeService;

    @PostMapping("/save")
    @Operation(summary = "部门新增",description = "新增")
    public ResVo save(@Validated @RequestBody DeptReq deptReq) {
        sysDeptService.saveDept(deptReq);
        return ResultVoUtil.success();
    }

    @PostMapping("/update")
    @Operation(summary = "部门更新",description = "更新")
    public ResVo update(@Validated @RequestBody DeptReq deptReq) {
        sysDeptService.updateDept(deptReq);
        return ResultVoUtil.success();
    }

    @PostMapping("/delete")
    @Operation(summary = "删除",description = "删除")
    @Parameters({
            @Parameter(name = "id",example = "1",description = "部门id",required = true,in = ParameterIn.QUERY),
    })
    public ResVo delete(@RequestParam("id") int id) {
        sysDeptService.deleteDept(id);
        return ResultVoUtil.success();
    }

    @GetMapping("/tree")
    @Operation(summary = "部门树",description = "树")
    public ResVo tree() {
        final List<DeptVo> deptTree = sysTreeService.getDeptTree();
        return ResultVoUtil.success(deptTree);
    }
}
