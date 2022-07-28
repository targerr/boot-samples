package com.example.controller;

import com.example.producer.Producer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: wgs
 * @Date 2022/7/28 10:45
 * @Classname MQTestController
 * @Description
 */
@RestController
@RequestMapping("/MQTest")
public class MQTestController {

    @Resource
    private Producer producer;

    @GetMapping("/sendMessage")
    public String sendMessage(@RequestParam("message") String message){
        producer.sendMessage("TestTopic", message);
        return "消息发送完成";
    }
}