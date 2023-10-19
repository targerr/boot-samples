package com.example.service;

import com.example.goods.GoodsInfo;

import java.util.List;

/**
 * @Author: wgs
 * @Date 2022/11/18 11:16
 * @Classname AsyncService
 * @Description 异步服务接口定义
 */
public interface AsyncService {
    /*
    异步保持商品信息
     */
    public abstract void asyncImportGoods(List<GoodsInfo> goodsInfos, String taskId);
}
