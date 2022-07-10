package com.example.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wgs
 * @Date 2022/7/6 14:06
 * @Classname RepeatSubmit
 * @Description 自定义注解防止表单重复提交
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmit {
    /**
     * 间隔时间(ms)，小于此时间视为重复提交
     */
    int interval() default 5000;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

}
