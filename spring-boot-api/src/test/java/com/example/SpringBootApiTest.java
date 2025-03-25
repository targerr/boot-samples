package com.example;

import com.alibaba.fastjson.JSON;
import com.example.entity.OAuth2Token;
import com.example.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootApiTest {

    @Autowired
    private AuthService authService;

    @Test
    public void contextLoads() {
        final OAuth2Token token = authService.getToken();
        System.out.println(JSON.toJSON(token));
    }

}
