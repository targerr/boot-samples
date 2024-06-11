package com.example.delayed;

import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author: wgs
 * @Date 2024/6/11 15:24
 * @Classname MessageSender
 * @Description 定义一个 RabbitMQ 消息发送者类，用来发送消息到 RabbitMQ：
 * 参考： https://zhuanlan.zhihu.com/p/641458427*
 */
@Slf4j
@Component
public class MessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendOrderMessage(OrderMessage message) {
        //为true,则交换机处理消息到路由失败，则会返回给生产者 配置文件指定，则这里不需指定
        rabbitTemplate.setMandatory(true);
        //开启强制消息投递（mandatory为设置为true），但消息未被路由至任何一个queue，则回退一条消息
        //消息是否成功被路由到队列，没有路由到队列时会收到回调（原setReturnCallback在2.0版本已过期）
//        rabbitTemplate.setReturnsCallback(returned -> {
//            int code = returned.getReplyCode();
//            System.out.println("code=" + code);
//            System.out.println("returned=" + returned);
//        });
        rabbitTemplate.convertAndSend(RabbitMQOrderConfig.ORDER_EXCHANGE, "order", message);

        log.info("===============延时队列生产消息====================");
        log.info("发送时间:{},发送内容:{}, {}ms后执行", LocalDateTime.now(), message, RabbitMQOrderConfig.DELAY_TIME);
    }



}
