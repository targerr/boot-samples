package com.example;

import com.example.config.properties.AliYunProperties;
import com.example.enums.SmsEnum;
import com.example.service.SmsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: wgs
 * @Date 2022/7/14 09:58
 * @Classname SmsTest
 * @Description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SmsTest {
    @Autowired
    private AliYunProperties aliYunProperties;
    @Autowired
    private SmsService smsService;

    @Test
    public void send() {
        smsService.send("18806513872", "1212", SmsEnum.用户登录);
    }
}