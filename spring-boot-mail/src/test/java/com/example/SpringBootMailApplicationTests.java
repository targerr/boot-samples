package com.example;

import com.example.utils.MailUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class SpringBootMailApplicationTests {

    @Autowired
    private MailUtils mailUtils;
    @Test
    public void send() {
        mailUtils.sendText("2313920848@qq.com","测试","测试邮箱发送");
    }

}
