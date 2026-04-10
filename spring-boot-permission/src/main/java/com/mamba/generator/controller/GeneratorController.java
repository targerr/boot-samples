package com.mamba.generator.controller;

import com.mamba.common.result.R;
import com.mamba.generator.service.CodeGeneratorService;
import com.mamba.generator.vo.GeneratorConfigVO;
import com.mamba.generator.vo.TableInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 代码生成器
 */
@Api(tags = "代码生成器")
@RestController
@RequestMapping("/api/generator")
public class GeneratorController {

    @Resource
    private CodeGeneratorService codeGeneratorService;

    @GetMapping("/tables")
    @ApiOperation(value = "查询数据库表列表")
    public R<List<TableInfoVO>> listTables() {
        return R.ok(codeGeneratorService.listTables());
    }

    @PostMapping("/generate")
    @ApiOperation(value =  "执行代码生成")
    public R<Void> generate(@RequestBody GeneratorConfigVO config) {
        codeGeneratorService.generate(config);
        return R.ok();
    }
}
