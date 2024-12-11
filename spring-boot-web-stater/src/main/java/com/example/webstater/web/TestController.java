package com.example.webstater.web;

import com.example.webstater.annotation.IgnoreResponseWrap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wgs
 * @Date 2024/10/18 17:09
 * @Classname TestController
 * @Description
 */
@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/success")
    public Boolean success() {
        return true;
    }

    @GetMapping("/fail")
    public Boolean fail() {
        return false;
    }

    @GetMapping("/fallback")
    @IgnoreResponseWrap // 忽略统一响应
    public String fallback() {
        return "fallback";
    }
}
