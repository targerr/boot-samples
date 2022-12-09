package com.example;

import com.alibaba.fastjson.JSON;
import com.example.service.IJWTService;
import com.example.util.TokenParseUtil;
import com.example.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: wgs
 * @Date 2022/11/8 16:26
 * @Classname JwtServiceTest
 * @Description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class JwtServiceTest {
    @Autowired
    private IJWTService ijwtService;

    @Test
    public void generateToke() throws Exception {
        final String token = ijwtService.generateToken("tom", "25d55ad283aa400af464c76d713c07ad");
        log.info("生成token: {}", token);

        LoginUserInfo userInfo = TokenParseUtil.parseUserInfoFromToken(token);
        log.info("parse token: [{}]", JSON.toJSONString(userInfo));
    }


}
