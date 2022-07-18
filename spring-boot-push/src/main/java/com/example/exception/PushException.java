package com.example.exception;

import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2022/7/18 14:41
 * @Classname PushException
 * @Description
 */
@Getter
public class PushException extends RuntimeException{
    private  Integer code;
    public PushException(Integer code ,String message){
        super(message);
        this.code = code;
    }
}
