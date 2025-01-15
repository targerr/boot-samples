package com.example.provider;

import cn.hutool.core.util.StrUtil;
import com.example.entity.AlarmTemplate;

/**
 * @Author: wgs
 * @Date 2025/1/14 17:05
 * @Classname BaseAlarmTemplateProvider
 * @Description
 */
public abstract class BaseAlarmTemplateProvider implements AlarmTemplateProvider {
    @Override
    public AlarmTemplate loadingAlarmTemplate(String templateId) {
        if (StrUtil.isEmpty(templateId)) {
            throw new RuntimeException("告警模板配置id不能为空");
        }
        return getAlarmTemplate(templateId);
    }

    /**
     * 查询告警模板
     *
     * @param templateId 模板id
     * @return AlarmTemplate
     */
    abstract AlarmTemplate getAlarmTemplate(String templateId);

}
