package com.example.service.communication;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: wgs
 * @Date 2022/11/25 16:01
 * @Classname RibbonConfig
 * @Description 使用 Ribbon 之前的配置, 增强 RestTemplate<
 */
@Component
public class RibbonConfig {
    /**
     * 注入 RestTemplate
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate (){
        return new RestTemplate();
    }
}
