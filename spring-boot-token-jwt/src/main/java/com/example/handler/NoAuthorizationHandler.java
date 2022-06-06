package com.example.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.enums.ErrorTypes;
import com.example.excetpion.NoAuthorization;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: wgs
 * @Date 2022/6/6 09:52
 * @Classname NoAuhorizationHandler
 * @Description
 */
@RestControllerAdvice
public class NoAuthorizationHandler {

    @ExceptionHandler(NoAuthorization.class)
    public JSONObject handlerException(NoAuthorization ex) {
        JSONObject json = new JSONObject();
        json.put("code", ErrorTypes.NO_LOGIN.getCode());
        json.put("msg", ErrorTypes.NO_LOGIN.getMsg());
        return json;

    }
}
