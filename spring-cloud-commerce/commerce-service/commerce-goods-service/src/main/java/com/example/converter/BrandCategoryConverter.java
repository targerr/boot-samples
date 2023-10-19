package com.example.converter;

import com.example.enums.BrandCategory;

import javax.persistence.AttributeConverter;

/**
 * @Author: wgs
 * @Date 2022/11/17 15:04
 * @Classname BrandCategoryConverter
 * @Description 品牌枚举类型转换器
 */
public class BrandCategoryConverter implements AttributeConverter<BrandCategory, String> {
    @Override
    public String convertToDatabaseColumn(BrandCategory brandCategory) {
        return brandCategory.getCode();
    }

    @Override
    public BrandCategory convertToEntityAttribute(String code) {
        return BrandCategory.of(code);
    }
}
