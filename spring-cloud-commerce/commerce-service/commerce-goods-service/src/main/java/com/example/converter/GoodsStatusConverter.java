package com.example.converter;

import com.example.enums.GoodsStatus;

import javax.persistence.AttributeConverter;

/**
 * @Author: wgs
 * @Date 2022/11/17 14:43
 * @Classname GoodsStatusConverter
 * @Description 商品状态枚举转换器
 */
public class GoodsStatusConverter implements AttributeConverter<GoodsStatus, Integer> {
    /**
     * 转化为可以传入数据库的基本类型
     *
     * @param goodsStatus
     * @return
     */
    @Override
    public Integer convertToDatabaseColumn(GoodsStatus goodsStatus) {
        return goodsStatus.getStatus();
    }

    /**
     * 还原数据库中字段对应的枚举
     *
     * @param integer
     * @return
     */
    @Override
    public GoodsStatus convertToEntityAttribute(Integer integer) {
        return GoodsStatus.of(integer);
    }
}
