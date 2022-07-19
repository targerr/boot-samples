package com.example.handler;

import com.example.param.SendRequest;
import com.example.service.PushSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: wgs
 * @Date 2022/7/18 14:50
 * @Classname SenderActivity
 * @Description 策略模式
 */
@Slf4j
@Service
public class SenderActivity {
    private static final Map<PushSender.PushSenderEnum, PushSender> PUSH_SENDER_MAP = new ConcurrentHashMap<>(4);

    /**
     * 构造器注入 代替工厂模式
     *
     * @param pushSenderList
     */
    public SenderActivity(List<PushSender> pushSenderList) {
        for (PushSender pushSender : pushSenderList) {
            PUSH_SENDER_MAP.put(pushSender.getPushSenderEnum(), pushSender);
        }
    }

    /**
     * 获取对应的实现
     *
     * @param pushSenderEnum
     * @return
     */
    public PushSender getSender(PushSender.PushSenderEnum pushSenderEnum) {
        return PUSH_SENDER_MAP.get(pushSenderEnum);
    }

    /**
     * 发送消息
     * @param pushSenderEnum
     * @param sendRequest
     */
    public void singleMsg(PushSender.PushSenderEnum pushSenderEnum, SendRequest sendRequest) {
        PushSender pushSender = getSender(pushSenderEnum);
        pushSender.singleMsg(sendRequest);
    }
}
