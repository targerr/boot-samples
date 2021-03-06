package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/6/18 10:40
 * @Classname QiniuOssYmlConfig
 * @Description qiniu oss 的yml配置参数
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "isys.oss.qiniu-oss")
public class QiniuOssYmlConfig {
    private String ak;
    private String sk;
    private String bucket;
}
