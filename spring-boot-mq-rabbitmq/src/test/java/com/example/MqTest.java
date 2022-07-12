package com.example;

import com.alibaba.fastjson.JSONObject;
import com.example.config.RabbitConsts;
import com.example.message.MessageStruct;
import org.assertj.core.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: wgs
 * @Date 2022/6/23 13:50
 * @Classname MqTest
 * @Description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MqTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void send() {
        JSONObject json = new JSONObject();
        json.put("code", 200);
        // rabbitTemplate.convertAndSend(RabbitKeys.EXCHANGE_DIRECT, RabbitKeys.QUEUE_HIT, json);
        rabbitTemplate.convertAndSend(RabbitConsts.QUEUE_PLAY, json);

    }

    @Test
    public void ttlMsg() {
//        String exchange = "direct-ttl-exchange";
//        String routingKey = "ttl";
        JSONObject json = new JSONObject();
        json.put("code", 200);
        //给MQ发送消息
        rabbitTemplate.convertAndSend("order-event-exchange","order.create.order",json);
//        rabbitTemplate.convertAndSend(exchange, routingKey, "延迟队列测试---不喝奶茶的Pogrammer");
    }




}
