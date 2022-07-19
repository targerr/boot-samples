package com.example;

import com.example.handler.SenderActivity;
import com.example.param.MessageParam;
import com.example.param.SendRequest;
import com.example.service.PushSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: wgs
 * @Date 2022/7/18 15:02
 * @Classname PushTest
 * @Description
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class PushTest {
    @Autowired
    private SenderActivity senderActivity;

    private static final String cid = "b227f948755e64597caca98a5a81f22a";

    @Test
    public void send() {
        SendRequest sendRequest = SendRequest.builder()
                .cid(cid)
                .messageParam(MessageParam.builder()
                        .title("测试头")
                        .body("极光推送")
                        .build())
                .build();

        // 个推
        senderActivity.singleMsg(PushSender.PushSenderEnum.GE_TUI, sendRequest);
        // 极光
        //  senderActivity.singleMsg(PushSender.PushSenderEnum.JI_GUANG, sendRequest);
    }
}
