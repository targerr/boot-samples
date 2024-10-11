package com.example.common.exception.code;

/**
 * @Author: wgs
 * @Date 2024/9/29 14:29
 * @Classname BaseExceptionCode
 * @Description
 * 异常编码
 */
public interface BaseExceptionCode {
    /**
     * 异常编码
     *
     * @return 异常编码
     */
    int getCode();

    /**
     * 异常消息
     *
     * @return 异常消息
     */
    String getMessage();
}
