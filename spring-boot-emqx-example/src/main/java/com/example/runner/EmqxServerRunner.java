package com.example.runner;

import com.example.config.MqttProperties;
import com.example.mqtt.MqttAcceptClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: wgs
 * @Date 2023/8/15 10:48
 * @Classname EmqxServerRunner
 * @Description mqtt 启动订阅来自其他客户端的消息
 */
@Component
@Slf4j
public class EmqxServerRunner implements CommandLineRunner {
    @Resource
    MqttAcceptClient mqttAcceptClient;
    @Resource
    MqttProperties mqttProperties;

    @Override
    public void run(String... args) {
        List<MqttProperties.SubscribeProperties> subscribes = mqttProperties.getSubscribes();
        if (CollectionUtils.isEmpty(subscribes)){
            log.warn("mqtt subscribes is empty");
            return;
        }
        for (MqttProperties.SubscribeProperties subscribeMap : subscribes) {
            try {
                mqttAcceptClient.subscribe(subscribeMap.getTopic(),subscribeMap.getOps());
            }catch (Exception e){
                log.error("mqtt subscribes topic:{} failed",subscribeMap.getTopic(),e);
            }
        }
    }
}
