package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class SpringBootLogLogbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootLogLogbackApplication.class, args);
    }

}
