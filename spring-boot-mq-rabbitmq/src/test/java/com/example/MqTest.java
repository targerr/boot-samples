package com.example;

import com.alibaba.fastjson.JSONObject;
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
        // rabbitTemplate.convertAndSend(RabbitKeys.EXCHANGE_DIRECT, RabbitKeys.QUEUE_HIT, json);
        for (int i = 0; i < 5; i++) {
            json.put("code", 200+i);
            System.err.println(200+i);

            rabbitTemplate.convertAndSend("fanout_sms_queue", JSONObject.toJSONString(json));
        }

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
