package com.mamba.generator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 代码生成器配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "generator")
public class GeneratorProperties {
    private String author = "mamba";
    private String packageName = "com.mamba.system";
    private String outputDir = System.getProperty("user.dir") + "/src/main/java";
    private String xmlOutputDir = System.getProperty("user.dir") + "/src/main/resources/mapper";
    private String tablePrefix = "sys_";
}
