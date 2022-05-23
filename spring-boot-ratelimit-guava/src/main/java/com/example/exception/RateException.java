package com.example.exception;

import com.example.enums.CommonEnum;
import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2022/5/22 14:45
 * @Classname RateException
 * @Description
 */
@Getter
public class RateException extends RuntimeException {
    private Integer code;

    public RateException(CommonEnum.ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public RateException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
