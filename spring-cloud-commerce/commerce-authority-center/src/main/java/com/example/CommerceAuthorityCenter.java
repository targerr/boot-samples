package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @Author: wgs
 * @Date 2022/11/8 15:28
 * @Classname com.example.CommerceAuthorityCenter
 * @Description 授权中心启动入口
 */
@EnableJpaAuditing
@EnableDiscoveryClient
@SpringBootApplication
public class CommerceAuthorityCenter {
    public static void main(String[] args) {
        SpringApplication.run(CommerceAuthorityCenter.class, args);
    }
}
