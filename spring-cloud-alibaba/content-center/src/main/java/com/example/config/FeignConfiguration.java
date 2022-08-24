package com.example.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/8/24 10:24
 * @Classname FeignConfiguration
 * @Description
 */
@Slf4j
@Configuration
public class FeignConfiguration implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        //获取当前线程环境
        // String dataSource = DynamicSourceTtl.get();
        //log.debug("RPC请求地址：{}，RPC环境参数：{}，当前数据库环境：{}",template.url(),dataSource,DynamicDataSourceContextHolder.peek());
        log.debug("RPC请求地址：{}",template.url());
        // 对消息头进行配置
        template.header("token", "123456");
    }
}
