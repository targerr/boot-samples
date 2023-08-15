package com.example.mqtt;

import com.alibaba.fastjson.JSON;
import com.example.config.MqttProperties;
import com.example.constants.MqttConstants;
import com.example.mqtt.callback.MqttSendCallback;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.UUID;

/**
 * @Author: wgs
 * @Date 2023/8/15 10:41
 * @Classname MqttSendClient
 * @Description  mqtt发送客户端
 */
@Component
@Slf4j
public class MqttSendClient {

    @Resource
    private MqttProperties mqttProperties;

    @Resource
    @Lazy
    private MqttSendCallback mqttSendCallback;

    @Setter
    @Getter
    private MqttClient mqttClient;

    /**
     * mqtt发送客户端
     */
    @PostConstruct
    public MqttClient connect() {
        MqttClient client = null;
        try {
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            client = new MqttClient(mqttProperties.getHostUrl(),uuid , new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(mqttProperties.getUsername());
            options.setPassword(mqttProperties.getPassword().toCharArray());
            options.setConnectionTimeout(mqttProperties.getTimeout());
            options.setKeepAliveInterval(mqttProperties.getKeepAlive());
            options.setCleanSession(true);
            options.setAutomaticReconnect(false);
            // 设置回调
            mqttSendCallback.setClientId(client.getClientId());
            try {
                // 设置回调
                client.setCallback(mqttSendCallback);
                IMqttToken iMqttToken = client.connectWithResult(options);
                boolean complete = iMqttToken.isComplete();
                log.info("mqtt发送客户端 连接"+(complete?"成功":"失败"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mqttClient = client;
        } catch (Exception e) {
            log.error("发送客户端链接MqTT异常",e);
        }
        return client;
    }


    /**
     * 发布消息
     * 主题格式： server:report:$orgCode(参数实际使用机构代码)
     * @param retained    是否保留
     * @param orgCode     orgId
     * @param pushMessage 消息体
     */
    public void publish(boolean retained, String orgCode, String pushMessage) {
        MqttMessage message = new MqttMessage();
        message.setQos(mqttProperties.getQos());
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        String sendTopic = MqttConstants.DEFAULT_PUBLISH_TOPIC_PREFIX + orgCode;
        log.info("sendTopic:{}",sendTopic);
//        String sendTopic = orgCode;
        MqttTopic topic = mqttClient.getTopic(sendTopic);
        if (topic == null){
            log.error("主题不存在:{}",sendTopic);
            throw new RuntimeException("主题不存在");
        }
        try {
            MqttDeliveryToken deliveryToken = topic.publish(message);
            deliveryToken.waitForCompletion();
        } catch (MqttException e) {
            log.error("发布消息,topic:{},message:{} failed",sendTopic,message,e);
        }
    }

    /**
     * 关闭连接
     * @param mqttClient mqttClient
     */
    public static void disconnect(MqttClient mqttClient) {
        try {
            if (mqttClient != null) {
                mqttClient.disconnect();
            }
        } catch (MqttException e) {
            log.error("sendMqttClient,disconnect fail",e);
        }
    }

    /**
     * 释放资源
     * @param mqttClient mqttClient
     */
    public static void close(MqttClient mqttClient) {
        try {
            if (mqttClient != null) {
                mqttClient.close();
            }
        } catch (MqttException e) {
            log.error("sendMqttClient,close fail",e);
        }
    }


    public void reconnection() {
        try {
            this.connect();
            log.info("mqtt发送客户端,重新连接成功");
        } catch (Exception e) {
            log.info("mqtt发送客户端,重新连接失败",e);
        }
    }
}
