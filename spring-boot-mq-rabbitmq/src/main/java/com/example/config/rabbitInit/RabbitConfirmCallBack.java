package com.example.config.rabbitInit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @Author: wgs
 * @Date 2022/11/1 16:53
 * @Classname RabbitConfirmCallBack
 * @Description
 */
@Slf4j
public class RabbitConfirmCallBack implements RabbitTemplate.ConfirmCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("消息唯一标识: {}", correlationData);
        log.info("确认状态: {}", ack);
        log.info("造成原因: {}", cause);
        System.err.println("````````````");
        if (ack) {
            log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
        } else {
            log.info("消息发送失败:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
        }
    }

}
