package com.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Author: wgs
 * @Date 2022/11/17 14:38
 * @Classname GoodsStatus
 * @Description 商品枚举状态
 */
@Getter
@AllArgsConstructor
public enum GoodsStatus {
    ONLINE(101, "上线"),
    OFFLINE(102, "下线"),
    STOCK_OUT(103, "缺货"),
    ;

    /** 状态码 */
    private final Integer status;

    /** 状态描述 */
    private final String description;

    /**
     * 根据 status 获取商品枚举
     *
     * @param status
     * @return
     */
    public static GoodsStatus of(Integer status) {

        Objects.requireNonNull(status);

        return Stream.of(GoodsStatus.values())
                .filter(e -> e.status.equals(status)).findAny()
                .orElseThrow(() -> new IllegalArgumentException(status + "不存在"));
    }
}
