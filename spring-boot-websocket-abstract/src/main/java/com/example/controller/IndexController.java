package com.example.controller;

import com.example.handler.OrderWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: wgs
 * @Date 2023/9/20 11:18
 * @Classname IndexController
 * @Description 进入websocket测试页面
 */
@RestController
@RequestMapping("/push")
public class IndexController {
    @Autowired
    private OrderWebSocketHandler orderHandler;

    @PostMapping("/order")
    public ResponseEntity<String> pushOrderMsg(@RequestParam String clientId, @RequestBody String msg) {
        try {
            orderHandler.sendToClient(clientId, msg);
            return ResponseEntity.ok("消息已发送");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("发送失败: " + e.getMessage());
        }
    }

}
