package com.example.controller;

import com.example.annotation.PassToken;
import com.example.annotation.UserLoginToken;
import com.example.common.JwtUtil;
import com.example.entity.User;
import com.example.excetpion.NoAuthorization;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: wgs
 * @Date 2022/6/6 09:36
 * @Classname LoginController
 * @Description
 */
@RestController
@RequestMapping("/")
public class LoginController {

    @PostMapping("/login")
    public String login(@RequestBody User user, HttpServletRequest request) {
        return JwtUtil.createToken(user);
    }

    @GetMapping("/get")
    public User getInfo(HttpServletRequest request) throws NoAuthorization {
        return JwtUtil.parse(request);
    }

    @GetMapping("/pass")
    @PassToken
    public String pass()   {
        return "跳过认证";
    }
}
