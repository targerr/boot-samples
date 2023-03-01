package com.example.sentineldemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wgs
 * @Date 2022/12/6 11:44
 * @Classname TestController
 * @Description
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/index")
    public String index(@RequestHeader(value = "token",required = false) String token) {

        System.err.println("token:" + token);

        return "success";
    }
}
