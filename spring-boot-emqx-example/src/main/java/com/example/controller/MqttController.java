package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.example.mqtt.MqttSendClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author zhangwenxue
 */
@RestController
@RequestMapping("/mqtt")
public class MqttController {

    @Resource
    private MqttSendClient mqttSendClient;

    @GetMapping(value = "/publishTopic")
    public Object publishTopic() {
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>(8);
        List<String> strings = Arrays.asList("张三", "李四", "赵四", "王五", "钱六");
        int i = ThreadLocalRandom.current().nextInt(strings.size());
        objectObjectHashMap.put("name",strings.get(i));
        objectObjectHashMap.put("time",new Date());
        objectObjectHashMap.put("vale",ThreadLocalRandom.current().nextInt(100));
        mqttSendClient.publish(false,"client:report:2", JSON.toJSONString(objectObjectHashMap));
        return JSON.toJSONString(objectObjectHashMap);
    }

}