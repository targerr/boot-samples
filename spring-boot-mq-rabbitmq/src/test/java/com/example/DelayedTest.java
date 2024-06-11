package com.example;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.delayed.MessageSender;
import com.example.delayed.OrderMessage;
import com.example.delayed.RabbitMQOrderConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: wgs
 * @Date 2024/6/11 15:44
 * @Classname DelayedTest
 * @Description 延时消息测试
 * 参考： https://zhuanlan.zhihu.com/p/641458427*
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DelayedTest {
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void send() {
        OrderMessage orderMessage = OrderMessage.builder()
                .orderId(RandomUtil.randomNumbers(20))
                .tradeNo(RandomUtil.randomNumbers(10))
                .build();

        messageSender.sendOrderMessage(orderMessage);
    }

    @Test
    public void test() {
        OrderMessage orderMessage = OrderMessage.builder()
                .orderId(RandomUtil.randomNumbers(20))
                .tradeNo(RandomUtil.randomNumbers(10))
                .build();

        String orderExchange = RabbitMQOrderConfig.ORDER_EXCHANGE;
        String orderRoutingKey = "order";
        rabbitTemplate.convertAndSend(orderExchange, orderRoutingKey, orderMessage, message -> {
            // 设置消息过期时间 10秒过期
            message.getMessageProperties().setExpiration("10000");
            return message;
        });


    }
}
