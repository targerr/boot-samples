package com.example.controller;

import com.example.config.RabbitConsts;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wgs
 * @Date 2022/11/1 15:30
 * @Classname ProducerController
 * @Description
 */
@RestController
public class ProducerController {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @RequestMapping("/sendMsg")
    public String sendFanoutMsg(String msg) {
        /**
         * 1.交换机名称
         * 2.路由key名称
         * 3.发送内容
         */
        amqpTemplate.convertAndSend("mt_ex", msg);
        return "success";
    }

    @RequestMapping("/sendQueueMsg")
    public String sendFanoutQueueMsg(String msg) {
        /**
         * 1.队列名称
         * 3.发送内容
         */
        amqpTemplate.convertAndSend("fanout_email_queue", msg);
        return "success";
    }

    @RequestMapping("/sendDirectQueueMsg")
    public String sendQueueMsg() {
        /**
         * 1.交换机名称
         * 2.路由key名称
         * 3.发送内容
         */
        amqpTemplate.convertAndSend(RabbitConsts.EXCHANGE_DIRECT, RabbitConsts.QUEUE_HIT, "测试~~~~");
        return "success";
    }
}
