package com.github.common.log.annotation;

import com.github.common.log.holder.SpringContextHolder;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author: wgs
 * @Date 2024/9/27 10:00
 * @Classname EnableSpringContextHolder
 * @Description
 * 自动注入SpringUtil
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SpringContextHolder.class)
public @interface EnableSpringContextHolder {
}
