package com.example.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Author: wgs
 * @Date 2022/8/17 14:18
 * @Classname CorsControll
 * @Description
 */
@RestController
@CrossOrigin(origins = "*")
public class CorsController {
    @GetMapping("/login")
    public String login(String name, String age, HttpSession session) {
        // 业务验证......
        session.setAttribute("user", name + ":" + age);

        return "login success!";
    }

    @GetMapping("/info")
    public String info(HttpSession session) {
        final Object user = session.getAttribute("user");

        return user.toString();
    }
}
