package com.github.echo.annotation;

import com.github.echo.configuration.HelloServiceAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author: wgs
 * @Date 2024/9/9 15:01
 * @Classname EnableHello
 * @Description 开启hello 自动配置
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(HelloServiceAutoConfiguration.class)
public @interface  EnableHello {
}
