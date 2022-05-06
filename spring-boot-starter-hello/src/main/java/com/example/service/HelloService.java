package com.example.service;

/**
 * @Author: wgs
 * @Date 2022/5/6 10:44
 * @Classname HelloService
 * @Description
 */
public class HelloService {
    private String name;
    private String address;

    public HelloService(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String sayHello() {
        return "你好！我的名字叫 " + name + "，我来自 " + address;
    }
}
