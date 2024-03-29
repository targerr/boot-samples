package com.example.enums;

import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2023/10/17 15:05
 * @Classname ResultEnum
 * @Description
 */
@Getter
public enum ResultEnum {
    /**
     * 成功*
     */
    SUCCESS(0, "成功"),

    PARAM_ERROR(1, "参数不正确"),

    PRODUCT_NOT_EXIST(10, "商品不存在"),

    PRODUCT_STOCK_ERROR(11, "商品库存不正确"),

    ORDER_NOT_EXIST(12, "订单不存在"),

    CART_EMPTY(18, "购物车为空"),

    ORDER_OWNER_ERROR(19, "该订单不属于给用户"),

    ORDER_STATUS_ERROR(20, "订单状态不正确"),

    ORDER_UPDATE_ERROR(21, "订单更新失败"),

    PRODUCT_UPDATE_ERROR(22, "更新商品失败"),

    PRODUCT_STATUS_ERROR(24, "商品状态不正确"),

    ORDER_CANCEL_SUCCESS(25,"成功取消订单"),

    ORDER_FINISH_SUCCESS(26,"成功完结订单"),
    LOGIN_FAIL(27,"登陆失败"),
    USER_NOT_EXISTS(28, "用户不存在"),
    USER_PWD_ERROR(29, "用户名or密码错误"),
    USER_NAME_ERROR(30, "用户名已经存在"),
    USER_EMAIL_ERROR(31, "邮箱已被占用"),
    USER_TEL_ERROR(32, "电话已被占用"),
    DEPT_NAME_ERROR(33, "同一层级下存在相同名称的部门"),
    DEPT_NOT_EXIST(34, "部门不存在"),
    DEPT_ROOT_ERROR(35, "根目录禁止修改"),
    DEPT_PARENT_ERROR(36, "父部门禁止移动到子部门"),
    DEPT_SUBSECTOR_ERROR(36, "当前部门下面有子部门，无法删除"),
    DEPT_USER_ERROR(36, "当前部门下面有用户，无法删除"),
    ROLE_NAME_ERROR(37, "角色名已存在"),
    ROLE_NOT_EXIST(37, "更新角色不存在"),
    ACL_MODULE_NAME_ERROR(33, "同一层级下存在相同名称"),
    ACL_NAME_ERROR(33, "当前权限模块下面存在相同名称的权限点"),
    ACL_NOT_EXIST(34, "权限不存在"),
    USER_ACL_NOT_EXIST(34, "用户操作未被授权"),
    ;
    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
