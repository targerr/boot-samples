package com.example.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @Author: wgs
 * @Date 2022/6/23 15:02
 * @Classname Consumer
 * @Description
 */
@Component
public class Consumer {

    @RabbitListener(queues="order.release.order.queue")
    public void getMessage(Object msg, Channel channel , Message message) throws IOException {
        System.out.println("消费时间："+ LocalDateTime.now());
        System.out.println("消费者接收到的消息："+msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
