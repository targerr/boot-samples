package com.example.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wgs
 * @Date 2023/10/13 14:37
 * @Classname ResVo
 * @Description
 */
@Data
public class ResVo<T> implements Serializable {
    /**
     * 错误码
     */
    private Integer code;
    /*错误信息*/
    private String msg;
    /*返回数据*/
    private T data;
}
