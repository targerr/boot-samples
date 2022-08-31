package com.example.controller;

import com.example.config.GetuiProperties;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TestController {

    @Autowired
    private GetuiProperties getuiProperties;

    @GetMapping("/config")
    public GetuiProperties getuiProperties(){
        return getuiProperties ;
    }
}
