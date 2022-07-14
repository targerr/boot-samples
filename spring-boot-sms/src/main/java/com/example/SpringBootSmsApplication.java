package com.example;

import com.example.config.properties.AliYunProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootSmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSmsApplication.class, args);
    }

}
