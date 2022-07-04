package com.example.exception;

import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2022/7/1 16:24
 * @Classname RateException
 * @Description
 */
@Getter
public class RateException extends RuntimeException {
    private Integer code;

    public RateException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
