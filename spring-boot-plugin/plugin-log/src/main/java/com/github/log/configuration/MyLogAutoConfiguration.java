package com.github.log.configuration;

import com.github.log.interceptor.MyLogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: wgs
 * @Date 2024/9/19 17:11
 * @Classname MyLogAutoConfiguration
 * @Description 配置类，用于自动配置拦截器、参数解析器等web组件
 */
@Configuration
public class MyLogAutoConfiguration implements WebMvcConfigurer {
    //注册自定义日志拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyLogInterceptor());
    }
}