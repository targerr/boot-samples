package com.example.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @Author: wgs
 * @Date 2022/11/1 14:13
 * @Classname FanoutEmailConsumer
 * @Description
 */

@Slf4j
@Component
@RabbitListener(queues = "fanout_sms_queue")
public class FanoutSmsConsumer {

    @RabbitHandler

    public void getMessage(Object msg, Channel channel , Message message)  {
        System.out.println("消费时间："+ LocalDateTime.now());
        System.out.println("短信消费者消息msg："+msg);
//
//        int i = 1;
//        final int i1 = i / 0;
    }


}
