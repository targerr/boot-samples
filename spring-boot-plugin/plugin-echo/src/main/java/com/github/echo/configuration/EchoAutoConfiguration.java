package com.github.echo.configuration;


import com.github.echo.core.EchoServiceImpl;
import com.github.echo.core.LoadService;
import com.github.echo.interceptor.EchoDataInterceptor;
import com.github.echo.properties.EchoProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @Author: wgs
 * @Date 2024/9/5 15:27
 * @Classname EchoAutoConfiguration
 * @Description 回显自动装配
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(EchoProperties.class)
public class EchoAutoConfiguration {

    private final EchoProperties echoProperties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = EchoProperties.DATA_ECHO_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
    public EchoServiceImpl echoService(Map<String, LoadService> loadServiceMap) {
        return new EchoServiceImpl(echoProperties, loadServiceMap);
    }



    @Bean
    public EchoDataInterceptor echoResultInterceptor(ApplicationContext applicationContext, EchoProperties echoProperties) {
        return new EchoDataInterceptor(applicationContext, echoProperties);
    }


}
