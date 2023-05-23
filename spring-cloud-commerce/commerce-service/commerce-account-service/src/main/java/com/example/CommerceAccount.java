package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @Author: wgs
 * @Date 2022/11/15 10:51
 * @Classname CommerceAccount
 * @Description <h1>用户账户微服务启动入口</h1>
 * 127.0.0.1:8003/ecommerce-account-service/swagger-ui.html
 * 127.0.0.1:8003/ecommerce-account-service/doc.html
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
public class CommerceAccount {
    public static void main(String[] args) {
        SpringApplication.run(CommerceAccount.class, args);
    }
}
