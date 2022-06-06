package com.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author: wgs
 * @Date 2022/6/6 09:29
 * @Classname ErrorTypes
 * @Description
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorTypes {
    NO_LOGIN(401,"未登录");

    private int code;
    private String msg;
}
