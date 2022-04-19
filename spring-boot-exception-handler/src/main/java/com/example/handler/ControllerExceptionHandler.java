package com.example.handler;

import com.example.excetpion.ServiceException;
import com.example.vo.ResultVO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: wgs
 * @Date 2022/4/19 10:32
 * @Classname ExceptionHandler
 * @Description
 */
//@RestControllerAdvice
@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public ResultVO handlerException(ServiceException e) {
        return ResultVO.error(e.getCode(), e.getMessage());
    }
    //拦截登录异常
   /* @ExceptionHandler(value = AuthorizeException.class)
    public ModelAndView handlerAuthorizeException() {
        return new ModelAndView("redirect:"
                .concat("/login"));
    }*/
}
