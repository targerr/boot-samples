package com.example.webstater.base.enums;

/**
 * description: 封装异常状态码
 */
public interface IErrorCode {

    /**
     * code
     *
     * @return String
     */
    String getErrorCode();

    /**
     * getMessage
     *
     * @return String
     */
    String getErrorMsg();
}
