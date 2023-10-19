package com.example.handler;

import cn.hutool.core.lang.Dict;
import com.example.exception.PreException;
import com.example.utils.ResultVoUtil;
import com.example.vo.ResVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: wgs
 * @Date 2023/10/17 17:19
 * @Classname SerExceptionHandler
 * @Description
 */
@RestControllerAdvice
@Slf4j
public class SerExceptionHandler {
    @ExceptionHandler(value = PreException.class)
    public ResVo handlerExceptionHandler(PreException e) {
        return ResultVoUtil.error(e.getCode(), e.getMessage());
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Dict handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                    HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return Dict.create().set("code", 500).set("msg", e.getMessage());
    }


    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public Dict handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return Dict.create().set("code", 500).set("msg", e.getMessage());
    }


    /**
     * 自定义验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Dict constraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        return Dict.create().set("code", 500).set("msg", message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResVo<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        log.error("【参数不合法】 uri:{} \n msg:{}", request.getRequestURI(),message);
        return ResultVoUtil.error(001, message);
    }


    @ExceptionHandler(BindException.class)
    public ResVo<?> bindExceptionHandler(BindException e, HttpServletRequest request) {
        List<String> msgList = dealBindResult(e.getBindingResult());
        String errorMsg = String.join(";", msgList);
        log.error("【参数不合法】 uri:{} \n msg:{}", request.getRequestURI(),errorMsg);
        return ResultVoUtil.error(001, errorMsg);
    }

    private List<String> dealBindResult(BindingResult bindingResult) {
        List<String> msgList = new ArrayList<>();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        for (ObjectError objectError : allErrors) {
            msgList.add(objectError.getDefaultMessage());
        }
        return msgList;
    }
}
