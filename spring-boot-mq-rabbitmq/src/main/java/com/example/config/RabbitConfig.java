package com.example.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * rabbit配置
 */
@Configuration
@Slf4j
public class RabbitConfig {
    @Autowired
    private CachingConnectionFactory connectionFactory;


    @Bean
    public Queue smsQueue() {
        return new Queue(RabbitConsts.FANOUT_SMS_QUEUE);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(RabbitConsts.FANOUT_EMAIL_QUEUE);
    }


    @Bean
    FanoutExchange fanoutExchange(){
        return new FanoutExchange(RabbitConsts.EXCHANGE_SPRINGBOOT_NAME);
    }


    // 绑定交换机 sms
    @Bean
    public Binding bindingSmsFanoutExchange(Queue  smsQueue,FanoutExchange fanoutSmsExchange){
        return BindingBuilder.bind(smsQueue).to(fanoutSmsExchange);
    }

    // 绑定交换机 email
    @Bean
    public Binding bindingEmailFanoutExchange(Queue emailQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(emailQueue).to(fanoutExchange);
    }

    @Bean
    public Queue getQueueHit() {
        return new Queue(RabbitConsts.QUEUE_HIT);
    }

    @Bean
    public Queue getQueuePlay() {
        return new Queue(RabbitConsts.QUEUE_PLAY);
    }

    /**
     * exchange
     */
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(RabbitConsts.EXCHANGE_DIRECT);
    }

    // 绑定队列于路由
    @Bean
    Binding bindingExchangeDirect() {
        return BindingBuilder.bind(getQueueHit()).to(directExchange()).with(RabbitConsts.QUEUE_HIT);
    }

    @Bean
    Binding bindingExchangeDirect2() {
        return BindingBuilder.bind(getQueuePlay()).to(directExchange()).with(RabbitConsts.QUEUE_PLAY);
    }


    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);

        template.setUseDirectReplyToContainer(false);
        //发送之前加一个拦截器，每次发送都会调用这个方法，方法名称已经说明了一切了
        template.setBeforePublishPostProcessors(new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //拦截逻辑添加环境变量 DynamicSourceTtl.get()
                final Map<String, Object> headers = message.getMessageProperties().getHeaders();
                headers .put("dataSource", "master");
                headers .put("tag", "vip");
                return message;
            }
        });
        // json序列化
        template.setMessageConverter(messageConverter());

        // 确认机制
        //若使用confirm-callback或return-callback，必须要配置publisherConfirms或publisherReturns为true
        //每个rabbitTemplate只能有一个confirm-callback和return-callback，如果这里配置了，那么写生产者的时候不能再写confirm-callback和return-callback
        //使用return-callback时必须设置mandatory为true，或者在配置中设置mandatory-expression的值为true
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(true);
        template.setMandatory(true);
        /**
         * 如果消息没有到exchange,则confirm回调,ack=false
         * 如果消息到达exchange,则confirm回调,ack=true
         * exchange到queue成功,则不回调return
         * exchange到queue失败,则回调return(需设置mandatory=true,否则不回回调,消息就丢了)
         */
        template.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("确认状态: {}", ack);
                if (ack) {
                    log.info("消息发送成功:消息唯一标识: ({}),确认状态({})", correlationData, ack);
                } else {
                    log.info("消息发送失败:消息唯一标识: ({}),确认状态({}),造成原因({})", correlationData, ack, cause);
                }
            }
        });
        template.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message);
            }
        });
        return template;
    }

    @Bean
    public MessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }



}