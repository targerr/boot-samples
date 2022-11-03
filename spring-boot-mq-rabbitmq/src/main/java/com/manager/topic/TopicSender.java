package com.manager.topic;
import com.manager.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/11/2 11:15
 * @Classname TopicSender
 * @Description
 */
@Component
public class TopicSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 第一个参数：TopicExchange名字
     * 第二个参数：Route-Key
     * 第三个参数：要发送的内容
     * @param user
     */
    public void send(User user) {
        this.rabbitTemplate.convertAndSend(
                TopicConfig.TOPIC_EXCHANGE,
                "dai.message",
                user);
        this.rabbitTemplate.convertAndSend(
                TopicConfig.TOPIC_EXCHANGE,
                "dai.dai",
                user);
    }
}
