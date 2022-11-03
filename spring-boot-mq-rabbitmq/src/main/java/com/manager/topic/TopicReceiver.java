package com.manager.topic;

import com.manager.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/11/2 11:14
 * @Classname TopicReceiver
 * @Description
 */
@Component
public class TopicReceiver {
    /**
     * queues是指要监听的队列的名字
     * @param user
     */
    @RabbitListener(queues = TopicConfig.TOPIC_QUEUE1)
    public void receiveTopic1(User user) {
        System.out.println("【receiveTopic1监听到消息】" + user.toString());
    }
    @RabbitListener(queues = TopicConfig.TOPIC_QUEUE2)
    public void receiveTopic2(User user) {
        System.out.println("【receiveTopic2监听到消息】" + user.toString());
    }
}
