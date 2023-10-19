package com.example.controller;

import com.example.dto.UserInfoDTO;
import com.example.req.DeptReq;
import com.example.service.SysDeptService;
import com.example.service.SysTreeService;
import com.example.utils.ResultVoUtil;
import com.example.vo.DeptVo;
import com.example.vo.ResVo;
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
public class DeptController {
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysTreeService sysTreeService;

    @PostMapping("/save")
    public ResVo save(@Validated @RequestBody DeptReq deptReq) {
        sysDeptService.saveDept(deptReq);
        return ResultVoUtil.success();
    }

    @PostMapping("/update")
    public ResVo update(@Validated @RequestBody DeptReq deptReq) {
        sysDeptService.updateDept(deptReq);
        return ResultVoUtil.success();
    }

    @PostMapping("/delete")
    public ResVo delete(@RequestParam("id") int id) {
        sysDeptService.deleteDept(id);
        return ResultVoUtil.success();
    }

    @GetMapping("/tree")
    public ResVo tree() {
        final List<DeptVo> deptTree = sysTreeService.getDeptTree();
        return ResultVoUtil.success(deptTree);
    }
}
