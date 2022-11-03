package com.manager.topic;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/11/2 11:14
 * @Classname TopicConfig
 * @Description topic到direct类型的Exchange路由规则是完全匹配binding key与routing key
 * 而通过topic可以进行模糊匹配
 * direct，区别是routing key是由一组以“.”分隔的单词组成，
 * 可以有通配符，“*”匹配一个单词，“#”匹配0个或多个单词；
 */
@Configuration
public class TopicConfig {

    /**
     * topic
     */
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String TOPIC_EXCHANGE = "topic.exchange";


    @Bean
    public Queue getTopicQueue1() {
        return new Queue(TOPIC_QUEUE1);
    }

    @Bean
    public Queue getTopicQueue2() {
        return new Queue(TOPIC_QUEUE2);
    }

    /**
     * 主题模式队列
     * <li>路由格式必须以 . 分隔，比如 user.email 或者 user.aaa.email</li>
     * <li>通配符 * ，代表一个占位符，或者说一个单词，比如路由为 user.*，那么 user.email 可以匹配，但是 user.aaa.email 就匹配不了</li>
     * <li>通配符 # ，代表一个或多个占位符，或者说一个或多个单词，比如路由为 user.#，那么 user.email 可以匹配，user.aaa.email 也可以匹配</li>
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding1() {
        return BindingBuilder
                // 设置queue
                .bind(getTopicQueue1())
                // 绑定交换机
                .to(topicExchange())
                // 设置routingKey
                .with("dai.message");
    }

    @Bean
    public Binding topicBinding2() {
        return BindingBuilder
                .bind(getTopicQueue2())
                .to(topicExchange())
                .with("dai.#");
    }
}
