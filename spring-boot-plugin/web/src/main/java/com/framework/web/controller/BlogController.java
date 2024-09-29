package com.framework.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.framework.web.entity.Blog;
import com.framework.web.service.IBlogService;
import com.github.echo.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wanggs
 * @since 2022-04-22
 */
@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private IBlogService iBlogService;
    @Autowired
    private HelloService helloService;

    @GetMapping("/getDetail")
    public Blog getDetail(String id) {

        return iBlogService.getById(id);
    }

    @GetMapping("/hello")
    public String hello(){
        return  helloService.sayHello();
    }

}
