package com.example.service;

import com.example.enums.SmsEnum;

/**
 * @Author: wgs
 * @Date 2022/7/14 09:54
 * @Classname SmsService
 * @Description
 */
public interface SmsService {

    public abstract Boolean send(String phone, String captcha, SmsEnum smsEnum);
}
