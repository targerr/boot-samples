package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: wgs
 * @Date 2023/9/20 11:18
 * @Classname IndexController
 * @Description  进入websocket测试页面
 */
@RestController
@RequestMapping("WebSocketController")
public class IndexController {
    @RequestMapping("init")
    public ModelAndView init(){
        return new ModelAndView("webSocket/webSocket");
    }
}
