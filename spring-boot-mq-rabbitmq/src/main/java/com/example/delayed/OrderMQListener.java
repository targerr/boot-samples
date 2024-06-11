package com.example.delayed;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: wgs
 * @Date 2024/6/11 15:25
 * @Classname OrderMQListener
 * @Description 定义一个 RabbitMQ 消息消费者类，用来接收并处理消息：
 * 参考： https://zhuanlan.zhihu.com/p/641458427*
 */

@Component
@Slf4j
@RabbitListener(queues = RabbitMQOrderConfig.ORDER_DEAD_LETTER_QUEUE)
public class OrderMQListener {
    @RabbitHandler
    public void consumer(OrderMessage orderMessage, Message message, Channel channel) throws IOException {
        log.info("收到消息：{}", DateUtil.now());
        log.info("msgTag：{}", message.getMessageProperties().getDeliveryTag());
        log.info("message：{}", message);
        log.info("content：{}", orderMessage);
    }
}
