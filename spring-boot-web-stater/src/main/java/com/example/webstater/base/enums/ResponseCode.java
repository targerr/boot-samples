package com.example.webstater.base.enums;

import cn.hutool.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2024/10/18 16:38
 * @Classname ResponseCode
 * @Description API响应操作码
 */
@Getter
@AllArgsConstructor
public enum ResponseCode  {

    /**
     * 成功
     */
    SUCCESS(HttpStatus.HTTP_OK, "请求成功"),

    /**
     * 业务异常
     */
    FAILURE(HttpStatus.HTTP_INTERNAL_ERROR, "请求失败"),

    ;

    final int code;

    final String message;
}
