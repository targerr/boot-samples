package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: wgs
 * @Date 2022/5/13 16:51
 * @Classname IndexController
 * @Description
 */
@Controller
@RequestMapping("/websocket")
public class IndexController {
    @Autowired
    private WebSocket webSocket;

    @ResponseBody
    @GetMapping("/send")
    public String index(String orderId){
        /*webSocket推送*/
        webSocket.sendMessage(orderId);
        return "ok";
    }

    @GetMapping("/list")
    public String list(){
        return "order/list";
    }
}
