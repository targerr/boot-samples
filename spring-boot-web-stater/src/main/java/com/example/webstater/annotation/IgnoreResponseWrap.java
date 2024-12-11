package com.example.webstater.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: wgs
 * @Date 2024/10/18 16:33
 * @Classname IgnoreResponseWrap
 * @Description 忽略响应结果
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreResponseWrap {

}
