package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wgs
 * @Date 2022/6/7 09:00
 * @Classname IndexController
 * @Description
 */
@RestController
@RequestMapping("/")
public class IndexController {
    @GetMapping("/index")
    public String index() {
        return "ok";
    }
}
