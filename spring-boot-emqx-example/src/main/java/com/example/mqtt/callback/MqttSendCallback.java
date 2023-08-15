package com.example.mqtt.callback;

import com.example.mqtt.MqttSendClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @Author: wgs
 * @Date 2023/8/15 10:49
 * @Classname MqttSendCallback
 * @Description  mqtt发送客户端的回调类
 */
@Component
@Slf4j
public class MqttSendCallback implements MqttCallbackExtended {

    @Setter
    @Getter
    private String clientId;

    @Resource
    private MqttSendClient mqttSendClient;



    /**
     * 客户端断开后触发
     * @param throwable throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {
        throwable.printStackTrace();
        log.info("连接断开 case:{}，正在重连",throwable.getCause().toString());
        if (mqttSendClient.getMqttClient() == null || !mqttSendClient.getMqttClient().isConnected()) {
            log.info("mqtt发送客户端的回调类 emqx重新连接");
            mqttSendClient.reconnection();
        }
    }

    /**
     * 客户端收到消息触发
     * @param topic       主题
     * @param mqttMessage 消息
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        // 后续业务逻辑可以在这里做分发 根据topic
        log.info("接收消息主题 : " + topic);
        log.info("接收消息Qos : " + mqttMessage.getQos());
        log.info("接收消息内容 : " + new String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        String[] topics = token.getTopics();
        if (topics == null || topics.length < 1){
            return;
        }
        for (String topic : topics) {
            log.info("向主题：" + topic + "发送消息成功！");
        }
        try {
            MqttMessage message = token.getMessage();
            byte[] payload = message.getPayload();
            String s = new String(payload, StandardCharsets.UTF_8);
            log.info("消息的内容是：" + s);
        } catch (MqttException e) {
            log.info("发布消息成功后的回调:{},异常",topics,e);
        }
    }

    @Override
    public void connectComplete(boolean b, String s) {
        log.info("ClientId:{}客户端连接成功！",clientId);
    }
}
