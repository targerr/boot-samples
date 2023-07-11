package com.example;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 监控中心服务器启动入口
 */
@EnableAdminServer
@SpringBootApplication
public class CommerceAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommerceAdminApplication.class, args);
    }

}
