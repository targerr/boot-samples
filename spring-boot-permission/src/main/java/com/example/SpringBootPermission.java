package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @Author: wgs
 * @Date 2023/9/26 11:31
 * @Classname SpringBootPermission
 * @Description
 */
@SpringBootApplication
@ServletComponentScan
@MapperScan({"com.example.mapper","com.example.modules.demo.dao"})
public class SpringBootPermission {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootPermission.class, args);
    }

}
