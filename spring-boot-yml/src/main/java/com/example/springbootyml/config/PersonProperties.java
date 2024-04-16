package com.example.springbootyml.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2024/4/16 10:21
 * @Classname PersonProperties
 * @Description
 */
@Component
@ConfigurationProperties(prefix = "person")
@Data
public class PersonProperties {
    String name;
    int age;
    Date birth;
    Map<String,Object> map;
    Dog dog;
    List<String> list;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Dog{
        private String name;
        private String age;

    }
}
