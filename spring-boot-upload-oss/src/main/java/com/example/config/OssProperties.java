package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/5/27 14:14
 * @Classname OssProperties
 * @Description
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssProperties {
    /**
     * OSS存储对象名称
     * demo
     */
    public String bucket;
    /**
     * 填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
     */
    private String endpoint;
    /**
     * 阿里云账号AccessKey
     */
    private String accessKeyId;
    /**
     * API密钥
     */
    private String accessKeySecret;

}
