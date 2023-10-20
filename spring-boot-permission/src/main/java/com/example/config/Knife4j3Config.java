//package com.example.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.CacheControl;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * @Author: wgs
// * @Date 2023/10/20
// * @Classname Knife4j3Config
// * @since 1.0.0
// */
//@EnableWebMvc
//@Configuration
//public class Knife4j3Config  implements WebMvcConfigurer {
//
//    /**
//     * 静态资源处理器配置
//     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
//                .setCacheControl(CacheControl.maxAge(5, TimeUnit.HOURS).cachePublic());
//    }
//}
