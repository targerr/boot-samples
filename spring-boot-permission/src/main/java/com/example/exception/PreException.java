package com.example.exception;

import com.example.enums.ResultEnum;
import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2023/9/26 14:40
 * @Classname PreException
 * @Description
 */
@Getter
public class PreException extends RuntimeException{
    private Integer code;
    public PreException(Integer code,String message){
        super(message);
        this.code = code;
    }

    public PreException(ResultEnum resultEnum){
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }
}
