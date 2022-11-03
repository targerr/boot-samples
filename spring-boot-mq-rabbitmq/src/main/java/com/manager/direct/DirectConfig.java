package com.manager.direct;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/11/2 11:05
 * @Classname DirectConfig
 * @Description 它会把消息路由到那些binding key与routing key完全匹配的Queue中
 * 根据routing key routing到相应的queue，
 * \routing不到任何queue的消息扔掉；
 * 可以不同的key绑到同一个queue，也可以同一个key绑到不同的queue；
 */
@Configuration
public class DirectConfig {

    /**
     * redirect模式
     */
    public static final String DIRECT_QUEUE1 = "direct.queue1";
    public static final String DIRECT_QUEUE2 = "direct.queue2";
    public static final String DIRECT_EXCHANGE = "direct.exchange";

    @Bean
    public Queue getDirectQueue() {
        return new Queue(DIRECT_QUEUE1);
    }

    @Bean
    public Queue getDirectQueue2() {
        return new Queue(DIRECT_QUEUE2);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    /**
     * direct模式
     * 消息中的路由键（routing key）如果和 Binding 中的 binding key 一致，
     * 交换器就将消息发到对应的队列中。路由键与队列名完全匹配
     *
     * @return
     */
    @Bean
    public Binding directBinding1() {
        return BindingBuilder
                // 设置queue
                .bind(getDirectQueue())
                // 绑定交换机
                .to(directExchange())
                // 设置routingKey
                .with("direct.V1");
    }


    @Bean
    public Binding directBinding2() {
        return BindingBuilder
                // 设置queue
                .bind(getDirectQueue2())
                // 绑定交换机
                .to(directExchange())
                // 设置routingKey
                .with("direct.V2");
    }

}
