package com.example.listener;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/7/28 10:44
 * @Classname Consumer
 * @Description
 */
@Component
@RocketMQMessageListener(consumerGroup = "MyConsumerGroup", topic = "TestTopic")
public class Consumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message)  {
        System.out.println("Recived message: " + message);
    }
}
