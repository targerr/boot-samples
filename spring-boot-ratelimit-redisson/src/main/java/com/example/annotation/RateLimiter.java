package com.example.annotation;

import com.example.enums.LimitType;

import java.lang.annotation.*;

/**
 * @Author: wgs
 * @Date 2022/7/1 15:53
 * @Classname RateLimiter
 * @Description 限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    /**
     * 限流key
     */
    String key() default "rate_limit:";

    /**
     * 限流时间,单位秒
     */
    int time() default 60;

    /**
     * 限流次数
     */
    int count() default 100;

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.DEFAULT;
}
