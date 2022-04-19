package com.example.enums;

import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2022/4/19 10:35
 * @Classname ResultEnum
 * @Description
 */
@Getter
public enum ResultEnum {
    /**
     * 成功
     */
    SUCCESS(0, "成功"),
    /**
     * 参数不正确
     */
    PARAM_ERROR(1, "参数不正确"),

    PRODUCT_NOT_EXIST(10, "商品不存在"),

    PRODUCT_STOCK_ERROR(11, "商品库存不正确"),

    ORDER_NOT_EXIST(12, "订单不存在"),

    CART_EMPTY(18, "购物车为空"),

    ORDER_OWNER_ERROR(19, "该订单不属于给用户"),

    ORDER_STATUS_ERROR(20, "订单状态不正确"),

    LOGIN_FAIL(21,"登陆失败"),
    ;
    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
