package com.example.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2025/3/25 10:18
 * @Classname ApiConfig
 * @Description 配置文件
 */
@Component
@ConfigurationProperties(prefix = "api")
@Data
public class ApiConfig {
    private String url;
    private String publicKey;
    private String clientId;
    private String clientSecret;
}
