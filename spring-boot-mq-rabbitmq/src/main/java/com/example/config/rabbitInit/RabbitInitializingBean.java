package com.example.config.rabbitInit;

import com.example.config.rabbitInit.RabbitBeforePublishPostProcessors;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/11/1 16:57
 * @Classname RabbitInitializingBean
 * @Description
 */
@Configuration
@AllArgsConstructor
public class RabbitInitializingBean implements InitializingBean {
    private RabbitTemplate rabbitTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
//        rabbitTemplate.setConfirmCallback(new RabbitConfirmCallBack());
//        rabbitTemplate.setReturnCallback(new RabbitReturnCallback());
        //      rabbitTemplate.setBeforePublishPostProcessors(new RabbitBeforePublishPostProcessors());
    }
}
