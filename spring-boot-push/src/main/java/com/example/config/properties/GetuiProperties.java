package com.example.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/7/18 13:46
 * @Classname GetuiProperties
 * @Description 个推配置
 */
@Data
@ConfigurationProperties(prefix = "push.getui")
@Component
public class GetuiProperties {
    /**
     * 应用id
     */
    private String appId;
    /**
     * 应用key
     */
    private String appKey;
    /**
     * 应用秘钥
     */
    private String masterSecret;
}
