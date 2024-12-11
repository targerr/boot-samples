package com.example.webstater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class WebStaterApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebStaterApplication.class, args);
    }


}
