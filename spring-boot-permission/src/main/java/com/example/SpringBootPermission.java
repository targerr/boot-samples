package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: wgs
 * @Date 2023/9/26 11:31
 * @Classname SpringBootPermission
 * @Description
 */
@SpringBootApplication
@MapperScan({"com.example.mapper","com.example.modules.demo.dao"})
public class SpringBootPermission {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootPermission.class, args);
    }

}
