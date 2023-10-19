package com.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Author: wgs
 * @Date 2022/11/17 14:50
 * @Classname GoodsCategory
 * @Description 品牌分类
 * 电器 -> 手机、电脑
 */
@Getter
@AllArgsConstructor
public enum GoodsCategory {

    DIAN_QI("10001", "电器"),
    JIA_JU("10002", "家具"),
    FU_SHI("10003", "服饰"),
    MY_YIN("10004", "母婴"),
    SHI_PIN("10005", "食品"),
    TU_SHU("10006", "图书"),
    ;


    /** 品牌分类编码 */
    private final String code;

    /** 品牌分类描述信息 */
    private final String description;

    // 根据 code 获取GoodsCategory
    public static GoodsCategory of(String code) {

        Objects.requireNonNull(code);

        return Stream.of(GoodsCategory.values())
                .filter(e -> e.code.equals(code)).findAny()
                .orElseThrow(() -> new IllegalArgumentException("商品分类不存在:" + code));

    }
}
