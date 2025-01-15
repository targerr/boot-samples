package com.example.provider;

import com.example.config.TemplateConfig;
import com.example.entity.AlarmTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * @Author: wgs
 * @Date 2025/1/14 17:17
 * @Classname YamlAlarmTemplateProvider
 * @Description @Primary 注解指定默认 Bean
 */
@Service
@Primary
public class YamlAlarmTemplateProvider extends BaseAlarmTemplateProvider {
    @Autowired
    private TemplateConfig templateConfig;

    @Override
    public AlarmTemplate getAlarmTemplate(String templateId) {
        Map<String, AlarmTemplate> configTemplates = templateConfig.getTemplates();
        AlarmTemplate alarmTemplate = configTemplates.get(templateId);
        if (ObjectUtils.isEmpty(alarmTemplate)) {
            throw new RuntimeException("未发现告警配置模板");
        }
        return alarmTemplate;
    }
}
