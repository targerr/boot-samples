package com.manager.fanout;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/11/2 11:11
 * @Classname FanoutConfig
 * @Description fanout类型的Exchange路由规则非常简单
 * 它会把生产者发送到该Exchange的所有消息路由到所有与它绑定的Queue中
 * 最终被多个消费者消费
 */
@Configuration
public class FanoutConfig {

    /**
     * fanout
     */
    public static final String FANOUT_QUEUE1 = "fanout.queue1";
    public static final String FANOUT_QUEUE2 = "fanout.queue2";
    public static final String FANOUT_EXCHANGE = "fanout.exchange";

    @Bean
    public FanoutExchange getFanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Queue getFanoutQueue1() {
        return new Queue(FANOUT_QUEUE1);
    }


    @Bean
    public Queue getFanoutQueue2() {
        return new Queue(FANOUT_QUEUE2);
    }


    /**
     * 监听fanout.exchange 的队列fanout.queue1
     * @return
     */
    @Bean
    public Binding fanoutBinding1() {
        return BindingBuilder
                // 设置queue
                .bind(getFanoutQueue1())
                // 绑定交换机
                .to(getFanoutExchange());
    }

    /**
     * 监听fanout.exchange 的队列fanout.queue2
     * @return
     */
    @Bean
    public Binding fanoutBinding2() {
        return BindingBuilder
                // 设置queue
                .bind(getFanoutQueue2())
                // 绑定交换机
                .to(getFanoutExchange());
    }

}
