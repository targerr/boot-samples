package com.example.controller;

import com.example.goods.GoodsInfo;
import com.example.service.async.AsyncTaskManager;
import com.example.vo.AsyncTaskInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: wgs
 * @Date 2022/11/25 13:59
 * @Classname AsyncGoodsController
 * @Description
 */
@Api(tags = "商品异步入库服务")
@Slf4j
@RestController
@RequestMapping("/async-goods")
public class AsyncGoodsController {
    private final AsyncTaskManager asyncTaskManager;

    public AsyncGoodsController(AsyncTaskManager asyncTaskManager) {
        this.asyncTaskManager = asyncTaskManager;
    }

    @ApiOperation(value = "导入商品", notes = "导入商品进入商品列表", httpMethod = "POST")
    @PostMapping("/import-goods")
    public AsyncTaskInfo importGoods(@RequestBody List<GoodsInfo> goodsInfos) {

        return asyncTaskManager.submitTask(goodsInfos);
    }

    @ApiOperation(value = "查询状态",notes = "查询异步执行任务状态",httpMethod = "GET")
    @GetMapping("/task-info")
    public AsyncTaskInfo getTaskInfo(@RequestParam String taskId){

        return asyncTaskManager.getTaskInfo(taskId);
    }
}
