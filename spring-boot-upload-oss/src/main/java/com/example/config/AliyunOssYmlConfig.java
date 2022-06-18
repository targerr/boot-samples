package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/6/18 09:37
 * @Classname AliyunOssYmlConfig
 * @Description
 */
@Data
@Configuration
@ConfigurationProperties(prefix="isys.oss.aliyun-oss")
public class AliyunOssYmlConfig {
    private String endpoint;
    private String publicBucketName;
    private String privateBucketName;
    private String accessKeyId;
    private String accessKeySecret;
}
