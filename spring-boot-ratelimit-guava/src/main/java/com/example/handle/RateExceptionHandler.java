package com.example.handle;

import cn.hutool.core.lang.Dict;
import com.example.exception.RateException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: wgs
 * @Date 2022/5/22 14:50
 * @Classname RateExcetionHandler
 * @Description
 */
@RestControllerAdvice
public class RateExceptionHandler {
    @ExceptionHandler(RateException.class)
    public Dict rateException(RateException rateException) {
        Dict dict = new Dict();
        dict.put("code", rateException.getCode());
        dict.put("msg", rateException.getMessage());
        return dict;

    }
}
