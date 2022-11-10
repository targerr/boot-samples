package com.example.listener;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
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
@RabbitListener(queues = "fanout_email_queue")
public class FanoutEmailConsumer {

    @RabbitHandler
//    public void process(String msg) {
//        log.info(">>邮件消费者消息msg:{}<<", msg);
//    }
    public void getMessage(Object msg, Channel channel , Message message) throws IOException {
        System.out.println("消费时间："+ LocalDateTime.now());
        System.out.println("邮件消费者消息msg："+msg);

        System.out.println(JSONObject.toJSONString(message.getMessageProperties().getHeaders()));

//        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
