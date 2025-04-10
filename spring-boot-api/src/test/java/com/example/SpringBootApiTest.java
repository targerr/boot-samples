package com.example;

import com.alibaba.fastjson.JSON;
import com.example.entity.OAuth2Token;
import com.example.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
