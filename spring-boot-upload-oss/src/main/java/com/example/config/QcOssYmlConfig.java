package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/6/18 09:37
 * @Classname AliyunOssYmlConfig
 * @Description aliyun oss 的yml配置参数
 */
@Data
@Configuration
@ConfigurationProperties(prefix="isys.oss.qc-oss")
public class QcOssYmlConfig {
    private String endpoint;
    private String publicBucketName;
    private String privateBucketName;
    private String secretId;
    private String secretKey;
}
