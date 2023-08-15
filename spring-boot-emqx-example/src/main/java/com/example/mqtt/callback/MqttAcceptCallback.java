package com.example.mqtt.callback;

import cn.hutool.core.date.DateUtil;
import com.example.mqtt.MqttAcceptClient;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * @Author: wgs
 * @Date 2023/8/15 10:44
 * @Classname MqttAcceptCallback
 * @Description mqtt接受服务的回调类
 */
@Component
@Slf4j
public class MqttAcceptCallback implements MqttCallbackExtended {
    
    @Resource
    private MqttAcceptClient mqttAcceptClient;


    @Override
    public void connectComplete(boolean b, String s) {
        log.info("mqtt接受服务的客户端 connectComplete ,host:{}",s);
    }

    /**
     * 客户端断开后触发
     * @param throwable throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("mqtt接受服务的客户端连接断开 case:{}，正在重连",throwable.getCause().toString());
        if (mqttAcceptClient.getMqttClient() == null || !mqttAcceptClient.getMqttClient().isConnected()) {
            mqttAcceptClient.reconnection();
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
        log.info("接收消息时间 : " + DateUtil.now());
    }

    /**
     * 发布消息成功后的回调
     * @param iMqttDeliveryToken token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        String[] topics = iMqttDeliveryToken.getTopics();
        if (topics == null || topics.length < 1){
            return;
        }
        for (String topic : topics) {
            log.info("向主题：" + topic + "发送消息成功！");
        }
        try {
            MqttMessage message = iMqttDeliveryToken.getMessage();
            byte[] payload = message.getPayload();
            String s = new String(payload, "UTF-8");
            log.info("消息的内容是：" + s);
        } catch (MqttException | UnsupportedEncodingException e) {
            log.info("发布消息成功后的回调:{},异常",topics,e);
        }
    }
}
