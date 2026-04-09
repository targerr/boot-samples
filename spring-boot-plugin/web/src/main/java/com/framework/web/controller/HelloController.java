package com.framework.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wgs
 * @CreateTime: 2026-04-08
 * @Description:
 * @Version 1.0
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    //    @MyLog(desc = "sayHello方法")
//@LogOperate(value = "获取博客详情", businessType = BusinessType.OTHER, saveRequestData = true)
    @GetMapping("/say")
    public String sayHello() {
        return "Hello World";
    }


}
