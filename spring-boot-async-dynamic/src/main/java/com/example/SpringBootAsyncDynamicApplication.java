package com.example;

import com.dtp.core.spring.EnableDynamicTp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDynamicTp
public class SpringBootAsyncDynamicApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAsyncDynamicApplication.class, args);
    }

}
