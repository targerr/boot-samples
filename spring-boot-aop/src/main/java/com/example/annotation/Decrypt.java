package com.example.annotation;

import com.example.analyze.ParamAnalyze;

import java.lang.annotation.*;

/**
 * @Author: wgs
 * @Date 2024/10/16 13:58
 * @Classname Decrypt
 * @Description  解密注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
@Documented
public @interface Decrypt {
    String algorithmId() default "sm4";

    String data() default "sm4";

    /**
     * @return 忽略所有验证，默认false。为true接口都不执行任何验证操作。
     */
    boolean ignoreValidate() default false;

    Class<?> handler() default ParamAnalyze.class;
}
