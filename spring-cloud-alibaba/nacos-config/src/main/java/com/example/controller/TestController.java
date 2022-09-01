package com.example.controller;

import com.example.config.GetuiProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wgs
 * @Date 2022/8/31 14:51
 * @Classname TestController
 * @Description
 */
@RestController
@RequestMapping("/")
@Slf4j
public class TestController {

    @Autowired
    private GetuiProperties getuiProperties;

    @Value("${app.secretKey}")
    private String secretKey;

    @GetMapping("/config")
    public GetuiProperties getuiProperties() {
        log.error("【共享配置】secretKey:{}", secretKey);
        return getuiProperties;
    }
}
