package com.example.handler;

import cn.hutool.core.lang.Dict;
import com.example.exception.SecKillException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: wgs
 * @Date 2022/7/20 15:35
 * @Classname GlobalExceptionHandler
 * @Description
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SecKillException.class)
    public Dict secKillException(SecKillException e){
        return Dict.create().set("code",500).set("msg", e.getMessage());
    }
}
