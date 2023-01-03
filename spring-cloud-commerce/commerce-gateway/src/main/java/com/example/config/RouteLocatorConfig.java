package com.example.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/11/10 16:58
 * @Classname RouteLocatorConfig
 * @Description配置登录请求转发规则
 */

@Configuration
public class RouteLocatorConfig {

    /**
     * <h2>使用代码定义路由规则, 在网关层面拦截下登录和注册接口</h2>
     */
    @Bean
    public RouteLocator loginRouteLocator(RouteLocatorBuilder builder) {

        // 手动定义 Gateway 路由规则需要指定 id、path 和 uri
        return builder.routes()
                .route(
                        "e-commerce_authority",
                        r -> r.path(
                                "/gateway/e-commerce/login",
                                "/gateway/e-commerce/register"
                        ).uri("http://localhost:9002/")
                ).build();
    }
}
