package com.example.common.exception;

/**
 * 异常接口类
 *
 * @Author: wgs
 * @Date 2024/9/29 15:50
 * @Classname BaseException
 * @Description
 */
public interface BaseException {

    /**
     * 统一参数验证异常码
     */
    int BASE_VALID_PARAM = -9;

    /**
     * 返回异常信息
     *
     * @return 异常信息
     */
    String getMessage();

    /**
     * 返回异常编码
     *
     * @return 异常编码
     */
    int getErrorCode();
}
