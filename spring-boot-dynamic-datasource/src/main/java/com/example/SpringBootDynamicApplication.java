package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: wgs
 * @Date 2024/4/23 14:24
 * @Classname SpringBootApplication
 * @Description
 */
@SpringBootApplication
@MapperScan({"com.example.mapper"})
public class SpringBootDynamicApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDynamicApplication.class, args);
    }
}
