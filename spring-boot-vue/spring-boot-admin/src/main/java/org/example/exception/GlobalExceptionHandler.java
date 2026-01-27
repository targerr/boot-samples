package org.example.exception;


import org.example.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Desc: 全局异常处理器
 * @Author: 
 * @date: 下午10:37 2022/3/10
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获全局异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result handler(Exception e){
        log.error("全局异常：",e);
        log.error("全局异常信息：",e.getMessage());
        return Result.fail(e.getMessage());
    }
}
