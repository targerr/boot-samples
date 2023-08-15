package com.example.mqtt;

import com.example.config.MqttProperties;
import com.example.mqtt.callback.MqttAcceptCallback;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @Author: wgs
 * @Date 2023/8/15 10:43
 * @Classname MqttAcceptClient
 * @Description  mqtt接受服务的客户端
 */
@Data
@Component
@Conditional(MqttCondition.class)
@Slf4j
public class MqttAcceptClient {
    

    @Resource
    @Lazy
    private MqttAcceptCallback mqttAcceptCallback;

    @Resource
    private MqttProperties mqttProperties;

    @Getter
    @Setter
    public MqttClient mqttClient;



    /**
     * 客户端连接
     */
    @PostConstruct
    public void connect() {
        MqttClient client;
        try {
            client = new MqttClient(mqttProperties.getHostUrl(), mqttProperties.getClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(mqttProperties.getUsername());
            options.setPassword(mqttProperties.getPassword().toCharArray());
            options.setConnectionTimeout(mqttProperties.getTimeout());
            options.setKeepAliveInterval(mqttProperties.getKeepAlive());
            options.setAutomaticReconnect(mqttProperties.getReconnect());
            options.setCleanSession(mqttProperties.getCleanSession());
            try {
                // 设置回调
                client.setCallback(mqttAcceptCallback);
                IMqttToken iMqttToken = client.connectWithResult(options);
                boolean complete = iMqttToken.isComplete();
                log.info("mqtt接受服务的客户端 连接"+(complete?"成功":"失败"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mqttClient = client;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新连接
     */
    public void reconnection() {
        try {
            mqttClient.connect();
            log.info("mqtt接受服务的客户端,重新连接成功");
        } catch (MqttException e) {
            log.info("mqtt接受服务的客户端,重新连接失败",e);
        }
    }

    /**
     * 订阅某个主题
     *
     * @param topic 主题
     * @param qos   连接方式
     */
    public void subscribe(String topic, int qos) {
        log.info("==============开始订阅{}主题==============", topic);
        try {
            mqttClient.subscribe(topic, qos);
        } catch (MqttException e) {
            log.info("订阅{}主题,失败",topic,e);
        }
    }


    /**
     * 取消订阅某个主题
     *
     * @param topic 取消订阅topic
     */
    public void unsubscribe(String topic) {
        log.info("==============开始取消订阅{}主题==============" , topic);
        try {
            mqttClient.unsubscribe(topic);
        } catch (MqttException e) {
            log.info("取消订阅{}主题,失败",topic,e);
        }
    }

}
