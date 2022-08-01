package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/7/29 10:41
 * @Classname DingDing
 * @Description
 */
@Component
@ConfigurationProperties(prefix = "dingding")
@Data
public class DingDingRobotProperties {
    /**
     * 密钥
     */
    private String secret;
    /**
     * 自定义群机器人中的 webhook
     */
    private String url;
}
