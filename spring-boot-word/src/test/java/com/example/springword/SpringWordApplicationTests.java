package com.example.springword;


import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringWordApplication.class)
@RunWith(SpringRunner.class)
public class SpringWordApplicationTests {
    @Autowired
    private SensitiveWordBs sensitiveWordBs;

    @Test
   public void contextLoads() {
        final String text = "大帅哥";
        System.out.println(sensitiveWordBs.findAll(text));
        System.out.println(sensitiveWordBs.replace(text));
    }

}
