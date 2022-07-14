package com.example.service;

import com.example.enums.SmsEnum;

/**
 * @Author: wgs
 * @Date 2022/7/14 09:54
 * @Classname SmsService
 * @Description
 */
public interface SmsScript {

    public abstract void send(String phone, String captcha, SmsEnum smsEnum);
}
