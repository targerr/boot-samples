package com.example.service;

import com.example.common.TableId;
import com.example.goods.DeductGoodsInventory;
import com.example.goods.GoodsInfo;
import com.example.goods.SimpleGoodsInfo;
import com.example.vo.PageSimpleGoodsInfo;

import java.util.List;

/**
 * @Author: wgs
 * @Date 2022/11/18 10:55
 * @Classname GoodsService
 * @Description 商品微服务相关接口定义
 */
public interface GoodsService {
    /**
     * <h2>根据 TableId 查询商品详细信息</h2>
     * */
    List<GoodsInfo> getGoodsInfoByTableId(TableId tableId);

    /**
     * <h2>获取分页的商品信息</h2>
     * */
    PageSimpleGoodsInfo getSimpleGoodsInfoByPage(int page);

    /**
     * <h2>根据 TableId 查询简单商品信息</h2>
     * */
    List<SimpleGoodsInfo> getSimpleGoodsInfoByTableId(TableId tableId);

    /**
     * <h2>扣减商品库存</h2>
     * */
    Boolean deductGoodsInventory(List<DeductGoodsInventory> deductGoodsInventories);
}
