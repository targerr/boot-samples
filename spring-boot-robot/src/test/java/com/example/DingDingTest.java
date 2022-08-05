package com.example;

import com.example.domin.ParamInfo;
import com.example.handler.DingDingActivity;
import com.example.handler.DingDingHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: wgs
 * @Date 2022/8/5 10:49
 * @Classname DingDingTest
 * @Description
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class DingDingTest {
    @Autowired
    private DingDingActivity dingDingActivity;

    @Test
    public void robotTest() {
        log.info("【钉钉机器人】 发送中~~~~~");
        dingDingActivity.doHandler(DingDingHandler.DingDingEnum.DING_DING_ROBOT, new ParamInfo());
        log.info("【钉钉机器人】 发送结束");
    }
}
