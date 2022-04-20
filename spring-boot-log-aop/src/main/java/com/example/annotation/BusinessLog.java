package com.example.annotation;

import com.example.enums.CommonEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志记录、自定义注解
 *
 * @author wgs
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BusinessLog {

    /**
     * 业务名称
     *
     * @return
     */
    String value() default "";

    /**
     * 用户行为
     *
     * @return
     */
    CommonEnum.LoggerTypeEnum behavior();

}