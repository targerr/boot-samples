package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2025/4/2 14:42
 * @Classname EncryptProperties
 * @Description
 */
@Configuration
@ConfigurationProperties(prefix = "encrypt")
@Data
public class EncryptProperties {
    /**
     * 表字段映射，指定表名和需要加密的字段
     */
    private Map<String, List<String>> tableFieldMap;

    /**
     * 是否启用拦截器
     */
    private Interceptor interceptor;

    @Data
    public static class Interceptor {
        /**
         * 是否启用加密拦截器
         */
        private boolean enabled;
        /**
         * 是否启用完整性校验
         */
        private boolean hmac;
    }
}
