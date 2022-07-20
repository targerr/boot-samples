package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.example.mapper")
@EnableScheduling //启用任务调度功能
public class SpringBootSeckillSimpleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSeckillSimpleApplication.class, args);
    }

}
