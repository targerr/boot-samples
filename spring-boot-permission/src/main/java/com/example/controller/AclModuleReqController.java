package com.example.controller;

import com.example.req.AclModuleReq;
import com.example.req.DeptReq;
import com.example.service.SysAclModuleService;
import com.example.service.SysTreeService;
import com.example.utils.ResultVoUtil;
import com.example.vo.ResVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: wgs
 * @Date 2023/10/20
 * @Classname AclModuleReq
 * @since 1.0.0
 */
@RestController
@RequestMapping("/aclModule")
@Tag(name = "权限模块控制类")
public class AclModuleReqController {
    @Resource
    private SysAclModuleService sysAclModuleService;
    @Resource
    private SysTreeService sysTreeService;


    @PostMapping("/save")
    @Operation(summary = "权限模块新增",description = "新增")
    public ResVo save(@Validated @RequestBody AclModuleReq aclModule) {
        sysAclModuleService.saveAclModule(aclModule);
        return ResultVoUtil.success();
    }

    @PostMapping("/update")
    @Operation(summary = "权限模块更新",description = "更新")
    public ResVo update(@Validated @RequestBody AclModuleReq aclModule) {
        sysAclModuleService.updateAclModule(aclModule);
        return ResultVoUtil.success();
    }

    @PostMapping("/delete")
    @Operation(summary = "权限模块删除",description = "删除")
    @Parameters({
            @Parameter(name = "id",example = "1",description = "权限模块id",required = true,in = ParameterIn.QUERY),
    })
    public ResVo delete(@RequestParam("id") int id) {
        sysAclModuleService.deleteAclModule(id);
        return ResultVoUtil.success();
    }
}
