package com.github.echo.configuration;

import com.github.echo.properties.HelloProperties;
import com.github.echo.service.HelloService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 你好服务自动配置
 * 配置类，基于Java代码的bean配置
 *
 * @author wanggaoshuai
 * @date 2022/05/06
 */
@Configuration
@EnableConfigurationProperties(HelloProperties.class)
public class HelloServiceAutoConfiguration {
    private HelloProperties helloProperties;

    /**
     * 通过构造方法注入配置属性对象HelloProperties
     * @param helloProperties
     */
    public HelloServiceAutoConfiguration(HelloProperties helloProperties) {
        this.helloProperties = helloProperties;
    }


    /**
     * 实例化HelloService并载入Spring IoC容器
     *
     * @return {@link HelloService}
     *
     * 如果用户没有自定义 HelloService，则创建一个默认的
     */
    @Bean
    @ConditionalOnMissingBean
    public HelloService helloService(){
        return new HelloService(helloProperties.getName(), helloProperties.getAddress());
    }
}
