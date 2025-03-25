package com.example.response;

import lombok.Data;

/**
 * @Author: wgs
 * @Date 2025/3/25 10:32
 * @Classname ResponseBody
 * @Description
 */
@Data
public class ResponseBody<T> {
    private Integer code;
    private Boolean success;
    private String msg;
    private T data;
}
