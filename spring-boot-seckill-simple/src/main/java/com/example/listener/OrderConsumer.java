package com.example.listener;

import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSONObject;
import com.example.constants.RabbitConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/7/20 16:26
 * @Classname OrserComsumer
 * @Description
 */
@Component
@Slf4j
public class OrderConsumer {
    @RabbitListener(queues = RabbitConstant.QUEUE_ORDER)
    public void getMessage(@Payload Map<String, String> data, @Headers Map<String, Object> headers,
                           Object msg, Channel channel, Message message) throws IOException {
        System.out.println("消费时间：" + LocalDateTime.now());
        System.out.println("消费者接收到的消息：" + JSONObject.toJSONString(data,true));
      //  channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
