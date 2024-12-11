package com.example.webstater.base.exception;

/**
 * @Author: wgs
 * @Date 2024/10/21 09:03
 * @Classname BaseException
 * @Description
 */
public interface BaseException {
    /**
     * 返回异常信息
     *
     * @return String
     */
    String getErrorMsg();

    /**
     * 返回异常编码
     *
     * @return String
     */
    String getErrorCode();
}
