package com.example.config;


import com.example.entity.AlarmTemplate;
import com.example.enums.TemplateChannel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * description:
 *
 * @Author: wgs
 * @Date 2025/1/14 17:17
 */
@Data
@ConfigurationProperties(prefix = TemplateConfig.PREFIX)
@Component
public class TemplateConfig {

    public static final String PREFIX = "spring.alarm.template";

    private boolean enabled = false;

    private TemplateChannel channel;
    /**
     * 模板列表
     */
    private Map<String, AlarmTemplate> templates;

    /**
     * 模板配置路径
     */
    private String templatePath;

}
