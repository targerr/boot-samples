package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author: wgs
 * @Date 2023/8/15 10:39
 * @Classname MqttProperties
 * @Description
 */
@Data
@Configuration
@ConfigurationProperties("mqtt")
public class MqttProperties {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 连接地址
     */
    private String hostUrl;

    /**
     * 客户端Id，同一台服务器下，不允许出现重复的客户端id
     */
    private String clientId;

    /**
     * 默认连接主题
     */
    private String defaultTopic;

    /**
     * 超时时间
     */
    private int timeout;

    /**
     * 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端
     * 发送个消息判断客户端是否在线，但这个方法并没有重连的机制
     */
    private int keepAlive;

    /**
     * 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连
     * 接记录，这里设置为true表示每次连接到服务器都以新的身份连接
     */
    private Boolean cleanSession;

    /**
     * 是否断线重连
     */
    private Boolean reconnect;

    /**
     * 启动的时候是否关闭mqtt
     */
    private Boolean isOpen;

    /**
     * 连接方式
     * 0 At most once，至多一次；消息发布完全依赖底层TCP/IP 网络，会发生消息丢失或者重复，这一级别可用于如下情况，环境，传感器数据，丢失一次度记录无所谓，因为不久之后会有第二次发送;
     * 1 At least once，至少一次；确保消息到达，但消息重复可能发生;
     * 2 Exactly once，确保只有一次。确保消息到达一次，这一级别可用于如下情况，在计费系统中，消息重复或者丢失导致不正确的结果。
     */
    private Integer qos;

    /**
     * 订阅topic
     */
    private List<SubscribeProperties> subscribes;

    @Data
    public static class SubscribeProperties{
        /**
         * 订阅topic
         */
        private String topic;

        /**
         * 1-保留 保留每次订阅会去拉取之前的消息  0-不保留
         */
        private Integer ops;
    }
}