package com.example.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.example.annotation.Timeout;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Author: wgs
 * @Date 2025/8/5 09:59
 * @Classname ApiController
 * @Description
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @Timeout(
            value = "${pack.app.api.timeout}",
            unit = TimeUnit.SECONDS,
            fallback = "fallbackQuery",
            retry = 3,
            retryDelay = 3000)
    @GetMapping("/query")
    public ResponseEntity<String> query() throws Throwable {
//        TimeUnit.SECONDS.sleep(new Random().nextInt(6)) ;
        ThreadUtil.sleep(5000L);
        return ResponseEntity.ok("success") ;
    }

    public ResponseEntity<String> fallbackQuery(Throwable e) {
        return ResponseEntity.ok("接口超时") ;
    }
}
