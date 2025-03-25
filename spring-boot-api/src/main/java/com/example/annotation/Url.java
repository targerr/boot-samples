package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Author: wgs
 * @Date 2025/3/25 10:17
 * @Classname Url
 * @Description 请求路径的注解
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE})
public @interface Url {
    String value();
}
