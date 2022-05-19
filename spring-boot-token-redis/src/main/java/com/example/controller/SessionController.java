package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wgs
 * @Date 2022/5/13 10:30
 * @Classname SessionController
 * @Description
 */
@RestController
@RequestMapping("/token")
public class SessionController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/login")
    public String login(@RequestParam String name, @RequestParam String password) {
        // 业务验证......
        // 账号密码正确
        String key = "token_" + UUID.randomUUID();
        stringRedisTemplate.opsForValue().set(key, name, 2, TimeUnit.DAYS);
        return "login success! token: " + key;
    }

    @GetMapping("/info")
    public String info(@RequestHeader String token) {
        return "当前登录的用户是: " + stringRedisTemplate.opsForValue().get(token);

    }
}
