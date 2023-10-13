package com.example.mdc;

import java.lang.annotation.*;

/**
 * @author YiHui
 * @date 2023/5/26
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MdcDot {
    String bizCode() default "";
}
