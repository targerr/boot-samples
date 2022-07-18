package com.example.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/7/18 13:46
 * @Classname GetuiProperties
 * @Description 极光推送配置
 */
@Data
@ConfigurationProperties(prefix = "push.jiguang")
@Component
public class JpushProperties {
    /**
     * 应用key
     */
    private String appKey;
    /**
     * 应用秘钥
     */
    private String masterSecret;
}
