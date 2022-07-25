package com.example.modules.demo.controller;

import com.example.common.constant.Constant;
import com.example.common.page.PageData;
import com.example.common.utils.Result;
import com.example.common.validator.AssertUtils;
import com.example.common.validator.ValidatorUtils;
import com.example.common.validator.group.AddGroup;
import com.example.common.validator.group.DefaultGroup;
import com.example.common.validator.group.UpdateGroup;
import com.example.modules.demo.dto.TOrderDTO;
import com.example.modules.demo.service.TOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * 
 *
 * @author wgs sunlightcs@gmail.com
 * @since 1.0.0 2022-07-25
 */
@RestController
@RequestMapping("demo/torder")
@Api(tags="")
public class TOrderController {
    @Autowired
    private TOrderService tOrderService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    public Result<PageData<TOrderDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<TOrderDTO> page = tOrderService.page(params);

        return new Result<PageData<TOrderDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<TOrderDTO> get(@PathVariable("id") Long id){
        TOrderDTO data = tOrderService.get(id);

        return new Result<TOrderDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public Result save(@RequestBody TOrderDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        tOrderService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    public Result update(@RequestBody TOrderDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        tOrderService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        tOrderService.delete(ids);

        return new Result();
    }

}