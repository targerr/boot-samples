package com.example.advice;

import com.example.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * <h2>全局异常捕获处理</h2>
 * */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

//    @ExceptionHandler(value = Exception.class)
//    public CommonResponse<String> handlerCommerceException(
//            HttpServletRequest req, Exception ex
//    ) {
//
//        CommonResponse<String> response = new CommonResponse<>(
//                -1, "business error"
//        );
//        response.setData(ex.getMessage());
//        log.error("commerce service has error: [{}]", ex.getMessage(), ex);
//        return response;
//    }
}
