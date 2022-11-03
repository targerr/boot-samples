package com.manager.direct;

import com.manager.User;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/11/2 11:07
 * @Classname DirectReceiver
 * @Description
 */
@Component
public class DirectReceiver {
    /**
     * queues是指要监听的队列的名字
     * @param user
     */
    @RabbitListener(queues = DirectConfig.DIRECT_QUEUE1)
    @RabbitHandler
    public void receiveDirect1(User user) {

        System.out.println("【receiveDirect1监听到消息】" + user);
    }

    /**
     * queues是指要监听的队列的名字
     * @param user
     */
    @RabbitListener(queues = DirectConfig.DIRECT_QUEUE2)
    @RabbitHandler
    public void receiveDirect2(User user) {

        System.out.println("【receiveDirect2监听到消息】" + user);
    }
}
