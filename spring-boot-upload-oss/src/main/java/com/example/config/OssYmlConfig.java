package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/6/18 09:37
 * @Classname OssYmlConfig
 * @Description
 */
@Data
@Configuration
@ConfigurationProperties(prefix="isys")
public class OssYmlConfig {

    @NestedConfigurationProperty //指定该属性为嵌套值, 否则默认为简单值导致对象为空（外部类不存在该问题， 内部static需明确指定）
    private Oss oss;

    /** 系统oss配置信息 **/
    @Data
    public static class Oss{

        /** 存储根路径 **/
        private String fileRootPath;

        /** 公共读取块 **/
        private String filePublicPath;

        /** 私有读取块 **/
        private String filePrivatePath;

        /** oss类型 **/
        private String serviceType;

    }
}