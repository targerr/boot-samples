package com.example.converter;

import com.example.enums.GoodsCategory;

import javax.persistence.AttributeConverter;

/**
 * @Author: wgs
 * @Date 2022/11/17 14:56
 * @Classname GoodsCategoryConverter
 * @Description 商品枚举转换
 */
public class GoodsCategoryConverter implements AttributeConverter<GoodsCategory,String> {
    /**
     * 转成数据库可以存储类型
     * @param goodsCategory
     * @return
     */
    @Override
    public String convertToDatabaseColumn(GoodsCategory goodsCategory) {
        return goodsCategory.getCode();
    }

    /**
     * 数据查出转换成枚举
     * @param code
     * @return
     */
    @Override
    public GoodsCategory convertToEntityAttribute(String code) {
        return GoodsCategory.of(code);
    }
}
