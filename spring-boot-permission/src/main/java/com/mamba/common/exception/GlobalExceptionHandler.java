package com.mamba.common.exception;

import com.mamba.common.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException ex) {
        log.warn("业务异常: {}", ex.getMessage());
        return R.fail(ex.getCode(), ex.getMessage());
    }

    /**
     * 参数校验异常 - @Valid @RequestBody
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        log.warn("参数校验异常: {}", message);
        return R.fail(400, message);
    }

    /**
     * 参数校验异常 - @Validated 方法参数
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<Void> handleConstraintViolationException(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        log.warn("约束校验异常: {}", message);
        return R.fail(400, message);
    }

    /**
     * 静态资源未找到异常 - 返回 404 状态码
     */
//    @ExceptionHandler(NoResourceFoundException.class)
//    public ResponseEntity<Void> handleNoResourceFoundException(NoResourceFoundException ex) {
//        log.debug("静态资源未找到: {}", ex.getResourcePath());
//        return ResponseEntity.notFound().build();
//    }

    /**
     * 兜底异常处理
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception ex) {
        log.error("系统异常", ex);
        return R.fail("系统异常，请联系管理员");
    }

}
