package com.example.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @Author: wgs
 * @Date 2022/6/6 09:58
 * @Classname WebAppConfigurer
 * @Description
 */
@Configuration
public class WebAppConfigurer extends WebMvcConfigurationSupport {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] patterns = new String[] { "/login","/error","/*.html","/swagger-resources/**"};
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                //.addPathPatterns("/user/**","/order/**")
                .excludePathPatterns(patterns);
        super.addInterceptors(registry);
    }

}