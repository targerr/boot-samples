package com.example.annotation;

import java.lang.annotation.*;

/**
 * @Author: wgs
 * @Date 2023/10/24
 * @Classname Permission
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {
}
