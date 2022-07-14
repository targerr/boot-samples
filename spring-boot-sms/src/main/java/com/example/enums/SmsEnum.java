package com.example.enums;

import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2022/6/9 13:45
 * @Classname SmsEnum
 * @Description
 */
@Getter
public enum SmsEnum {
    //用户登录
    用户登录("用户登录","SMS_154950909"),
    //用户注册
    用户注册("用户注册","SMS_139795094"),
    //修改密码
    修改密码("修改密码","SMS_139795093"),
    //用户信息变更
    用户信息变更("用户信息变更","SMS_139795092"),;

    private String state;
    private String value;

    SmsEnum(String state, String value) {
        this.state = state;
        this.value = value;
    }
}
