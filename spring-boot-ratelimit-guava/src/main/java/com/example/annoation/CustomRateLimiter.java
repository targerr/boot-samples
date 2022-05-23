package com.example.annoation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wgs
 * @Date 2022/5/22 09:05
 * @Classname RateLimit
 * @Description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomRateLimiter {
    int NOT_LIMITED = 0;

    /**
     * qps
     */
    @AliasFor("qps") double value() default NOT_LIMITED;

    /**
     * qps
     */
    @AliasFor("value") double qps() default NOT_LIMITED;

    /**
     * 超时时长
     */
    int timeout() default 0;

    /**
     * 超时时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
