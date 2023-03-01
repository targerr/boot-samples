package com.example.sentineldemo.controller;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * <h2>全局异常捕获处理</h2>
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public String handlerCommerceException(
            HttpServletRequest req, Exception ex
    ) {

        return ex.getMessage();
    }
}
