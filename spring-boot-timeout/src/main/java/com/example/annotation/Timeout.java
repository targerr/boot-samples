package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wgs
 * @Date 2025/8/4 13:43
 * @Classname Timeout
 * @Description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Timeout {

    // 基础超时时间（支持 SpEL 表达式，如 "${pack.app.xxx.timeout}"）
    String value() default "5000";
    // 时间单位
    TimeUnit unit() default TimeUnit.MILLISECONDS ;
    // 重试次数（默认不重试）
    int retry() default 0;
    // 重试间隔（毫秒）
    long retryDelay() default 0 ;
    // 降级方法名（需在同一类中）
    String fallback() default "";
    // 线程池名称（指向配置的线程池 Bean）
    String executor() default "timeoutExecutor";
}
