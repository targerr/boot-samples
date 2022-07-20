package com.example.exception;

import lombok.Data;

/**
 * @Author: wgs
 * @Date 2022/7/20 15:02
 * @Classname SecKillException
 * @Description
 */
@Data
public class SecKillException extends RuntimeException{
    private  Integer code;

    public SecKillException(String message){
        super(message);
    }

    public SecKillException(Integer code,String message){
        super(message);
        this.code = code;
    }
}
