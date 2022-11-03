package com.manager.fanout;

import com.manager.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/11/2 11:12
 * @Classname FanoutReceiver
 * @Description
 */
@Component
public class FanoutReceiver {
    /**
     * queues是指要监听的队列的名字
     * @param user
     */
    @RabbitListener(queues = FanoutConfig.FANOUT_QUEUE1)
    public void receiveTopic1(User user) {
        System.out.println("【receiveFanout1监听到消息】" + user);
    }

    @RabbitListener(queues = FanoutConfig.FANOUT_QUEUE2)
    public void receiveTopic2(User user) {
        System.out.println("【receiveFanout2监听到消息】" + user);
    }
}
