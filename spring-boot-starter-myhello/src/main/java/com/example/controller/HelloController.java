package com.example.controller;

import com.example.log.MyLog;
import com.example.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wgs
 * @Date 2022/5/6 10:57
 * @Classname HelloController
 * @Description
 */
@RestController
@RequestMapping("/hello")
public class HelloController {
    //HelloService在我们自定义的starter中已经完成了自动配置，所以此处可以直接注入
    @Autowired
    private HelloService helloService;

    @GetMapping("/say")
    @MyLog(desc = "sayHello方法") //日志记录注解
    public String sayHello() {
        return helloService.sayHello();
    }
}
