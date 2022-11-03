package com.manager.fanout;


import com.manager.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/11/2 11:13
 * @Classname FanoutSender
 * @Description
 */
@Component
public class FanoutSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 消息发送到FanoutConfig.FANOUT_EXCHANGE交换机中
     * @param user
     */
    public void send(User user) {
        this.rabbitTemplate
                .convertAndSend(FanoutConfig.FANOUT_EXCHANGE, "", user);
    }
}
