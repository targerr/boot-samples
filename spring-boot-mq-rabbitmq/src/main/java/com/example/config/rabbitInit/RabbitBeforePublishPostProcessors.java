package com.example.config.rabbitInit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @Author: wgs
 * @Date 2022/11/1 16:55
 * @Classname RabbitReturnCallback
 * @Description
 */
@Slf4j
public class RabbitBeforePublishPostProcessors implements MessagePostProcessor {

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        //拦截逻辑添加环境变量 DynamicSourceTtl.get()
        message.getMessageProperties().getHeaders().put("dataSource", "master");
        return message;
    }
}
