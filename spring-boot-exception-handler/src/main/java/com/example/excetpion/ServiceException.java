package com.example.excetpion;

import com.example.enums.ResultEnum;
import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2022/4/19 10:33
 * @Classname ServiceHandler
 * @Description
 */
@Getter
public class ServiceException extends RuntimeException {
    private Integer code;

    public ServiceException(Integer code, String message){
        super(message);
        this.code = code;
    }
    public ServiceException(ResultEnum resultEnum){
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();

    }
}
