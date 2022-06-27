package com.example.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/6/24 10:50
 * @Classname WxPayBean
 * @Description
 */
@Data
@Configuration
@ConfigurationProperties(prefix="wxpay")
public class WxPayBean {
    private String appId;
    private String appSecret;
    private String mchId;
    private String partnerKey;
    private String certPath;
    private String domain;
}
