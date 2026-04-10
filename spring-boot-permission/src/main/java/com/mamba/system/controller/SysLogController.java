package com.mamba.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.mamba.common.base.BaseController;
import com.mamba.common.result.PageResult;
import com.mamba.common.result.R;
import com.mamba.system.dto.PageQueryDTO;
import com.mamba.system.service.SysLogService;
import com.mamba.system.vo.SysLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 操作日志
 */
@Slf4j
@RestController
@RequestMapping("/api/system/log")
@Api(tags = "操作日志")
public class SysLogController extends BaseController {

    @Resource
    private SysLogService sysLogService;

    /**
     * 分页查询操作日志
     */
    @GetMapping("/page")
    @ApiOperation(value ="分页查询操作日志")
    public R<PageResult<SysLogVO>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String keyword) {
        PageQueryDTO queryDTO = new PageQueryDTO();
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);
        queryDTO.setType(type);
        queryDTO.setKeyword(keyword);
        return R.ok(sysLogService.page(queryDTO));
    }

    /**
     * 复原操作
     */
    @PutMapping("/restore/{id}")
    @ApiOperation(value ="复原操作")
    @SaCheckLogin
    public R<Void> restore(@PathVariable Integer id) {
        sysLogService.restore(id);
        return R.ok();
    }
}
