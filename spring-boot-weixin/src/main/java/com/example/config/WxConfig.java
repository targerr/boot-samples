package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/6/21 10:18
 * @Classname WxConfig
 * @Description
 */
@Data
@Component
@ConfigurationProperties(prefix = "wxpay")
public class WxConfig {
    private String appId;
    private String appSecret;
    private String mchId;
    private String partnerKey;
    private String certPath;
    private String domain;
    private String notifyUrl;

}