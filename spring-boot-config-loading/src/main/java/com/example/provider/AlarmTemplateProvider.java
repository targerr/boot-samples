package com.example.provider;


import com.example.entity.AlarmTemplate;

/**
 * @Author: wgs
 * @Date 2025/1/14 17:05
 * @Classname AlarmTemplateProvider
 * @Description
 */
public interface AlarmTemplateProvider {
    /**
     * 加载告警模板
     *
     * @param templateId 模板id
     * @return AlarmTemplate
     */
    public AlarmTemplate loadingAlarmTemplate(String templateId);
}
