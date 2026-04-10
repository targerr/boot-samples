package com.mamba.common.exception;

import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int code = 500;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

}
