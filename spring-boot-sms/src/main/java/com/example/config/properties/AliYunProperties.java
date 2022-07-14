package com.example.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/7/14 09:46
 * @Classname AliYunProperties
 * @Description
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ali.config")
public class AliYunProperties {
    /**
     * 阿里云accessKeyId
     */
    private String accessKeyId;
    /**
     * 阿里云accessKeySecret
     */
    private String accessKeySecret;

    //指定该属性为嵌套值, 否则默认为简单值导致对象为空（外部类不存在该问题， 内部static需明确指定）
    @NestedConfigurationProperty
    private Sms sms;

    /**
     * 短信配置
     */
    @Data
    public static class Sms{
        /**
         * 短信服务签名
         */
        private String signName;
        /**
         * 短信模板code
         */
        private String templateCode;
    }
}
