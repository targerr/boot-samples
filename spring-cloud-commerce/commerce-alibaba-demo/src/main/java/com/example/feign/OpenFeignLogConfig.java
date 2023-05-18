package com.example.feign;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/12/6 14:42
 * @Classname OpenFeignLogConfig
 * @Description 开启 openfeign 日志
 */
@Configuration
public class OpenFeignLogConfig {
    @Bean
    Logger.Level feignLoggerLeave(){
        return Logger.Level.FULL;
    }
}
