package com.example.common.exception;

import com.example.common.exception.code.ResponseCode;
import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2024/9/25 15:12
 * @Classname BusinessException
 * @Description
 */
@Getter
public class BizException extends RuntimeException {
    private Integer code;

    public BizException(ResponseCode responseCode) {
        super(responseCode.getMessage());

        this.code = responseCode.getCode();
    }

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(String message) {
        super(message);
    }
}
