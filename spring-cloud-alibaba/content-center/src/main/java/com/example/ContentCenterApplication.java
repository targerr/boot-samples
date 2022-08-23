package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan({"com.example.*.mapper"})
public class ContentCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication.class, args);
    }


    /**
     * 在Spring 容器中,创建一个对象,类型是RestTemplate,方法名或者ID 是restTemplate
     * 相当于<bean id = "restTemplate" class="xxx.RestTemplate"></bean>
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate (){
        return new RestTemplate();
    }

}
