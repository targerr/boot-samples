package com.example;

import com.example.binding.echo.EchoDataInterceptor;
import com.example.binding.echo.properties.EchoProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.example.mapper")
public class SpringBootMybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMybatisPlusApplication.class, args);
    }


    @Bean
    public EchoDataInterceptor echoResultInterceptor(ApplicationContext applicationContext, EchoProperties echoProperties) {
        return new EchoDataInterceptor(applicationContext, echoProperties);
    }


}
