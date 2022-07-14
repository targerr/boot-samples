package com.example.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: wgs
 * @Date 2022/7/14 13:56
 * @Classname SmsScriptHandler
 * @Description 标识 短信渠道
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface SmsScriptHandler {
    /**
     * 这里输入脚本名
     */
    String value();
}
