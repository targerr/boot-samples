package com.example.mqtt;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Author: wgs
 * @Date 2023/8/15 10:42
 * @Classname MqttCondition
 * @Description
 */
public class MqttCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //1、能获取到ioc使用的beanfactory
        context.getBeanFactory();
        //2、获取类加载器
        context.getClassLoader();
        //3、获取当前环境信息
        Environment environment = context.getEnvironment();
        String isOpen = environment.getProperty("mqtt.isOpen");
        return Boolean.parseBoolean(isOpen);
    }
}
