package com.manager.direct;

import com.manager.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/11/2 11:09
 * @Classname DirectSender
 * @Description
 */
@Component
public class DirectSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send1(User user) {
        this.rabbitTemplate.convertAndSend(
                DirectConfig.DIRECT_EXCHANGE,
                // routingKey
                "direct.V1",
                user);
    }

    public void send2(User user) {
        this.rabbitTemplate.convertAndSend(
                DirectConfig.DIRECT_EXCHANGE,
                // routingKey
                "direct.V2",
                user);
    }
}
