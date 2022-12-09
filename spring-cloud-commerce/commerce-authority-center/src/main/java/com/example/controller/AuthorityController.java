package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.example.annotation.IgnoreResponseAdvice;
import com.example.service.IJWTService;
import com.example.vo.JwtToken;
import com.example.vo.UserNameAndPassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wgs
 * @Date 2022/11/10 09:13
 * @Classname AuthorityController
 * @Description 对外暴露的授权服务接口
 */
@Slf4j
@RestController
@RequestMapping("/authority")
public class AuthorityController {
    private final IJWTService ijwtService;

    public AuthorityController(IJWTService ijwtService) {
        this.ijwtService = ijwtService;
    }

    /**
     * <h2>从授权中心获取 Token (其实就是登录功能), 且返回信息中没有统一响应的包装</h2>
     */
    @IgnoreResponseAdvice
    @PostMapping("/token")
    public JwtToken token(@RequestBody UserNameAndPassword usernameAndPassword) throws Exception {

        if (1==1){
            throw new RuntimeException("异常了");
        }

        log.info("request to get token with param: [{}]", JSON.toJSONString(usernameAndPassword));
        return new JwtToken(ijwtService.generateToken(
                usernameAndPassword.getUsername(),
                usernameAndPassword.getPassword()
        ));
    }

    /**
     * <h2>注册用户并返回当前注册用户的 Token, 即通过授权中心创建用户</h2>
     */
    @IgnoreResponseAdvice
    @PostMapping("/register")
    public JwtToken register(@RequestBody UserNameAndPassword usernameAndPassword) throws Exception {

        log.info("register user with param: [{}]", JSON.toJSONString(usernameAndPassword));
        return new JwtToken(ijwtService.registerUserAndGenerateToken(
                usernameAndPassword
        ));
    }
}
