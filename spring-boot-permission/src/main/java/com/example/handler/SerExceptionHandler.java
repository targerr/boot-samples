package com.example.handler;

import com.example.exception.PreException;
import com.example.utils.ResultVoUtil;
import com.example.vo.ResVo;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: wgs
 * @Date 2023/10/17 17:19
 * @Classname SerExceptionHandler
 * @Description
 */
@RestControllerAdvice
public class SerExceptionHandler {
    @ExceptionHandler(value = PreException.class)
    @ResponseBody
    public ResVo handlerExceptionHandler(PreException e) {
        return ResultVoUtil.error(e.getCode(), e.getMessage());
    }

}
