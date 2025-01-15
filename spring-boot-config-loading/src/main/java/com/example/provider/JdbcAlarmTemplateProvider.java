package com.example.provider;


import com.example.entity.AlarmTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.function.Function;

/**
 * description: 数据库告警模板加载
 * @Author: wgs
 * @Date 2025/1/14 17:17
 */
@RequiredArgsConstructor
public class JdbcAlarmTemplateProvider extends BaseAlarmTemplateProvider {

    private final Function<String, AlarmTemplate> function;

    @Override
    public AlarmTemplate getAlarmTemplate(String templateId) {
        AlarmTemplate alarmTemplate = function.apply(templateId);
        if (ObjectUtils.isEmpty(alarmTemplate)) {
            throw new RuntimeException("未发现告警配置模板");
        }
        return alarmTemplate;
    }
}
