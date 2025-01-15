package com.example.config;

import com.example.entity.AlarmTemplate;
import com.example.provider.JdbcAlarmTemplateProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

/**
 * @Author: wgs
 * @Date 2025/1/14 18:46
 * @Classname StartConfig
 * @Description
 */
@Slf4j
@Configuration
public class StartConfig {
    @Bean
    public JdbcAlarmTemplateProvider jdbcAlarmTemplateProvider(){

        Function<String, AlarmTemplate> function = templateId -> {
            // 假设这里是从数据库或缓存中获取数据
            // 示例中仅返回一个假设的模板
            if ("send-message-notice".equals(templateId)) {
                return new AlarmTemplate(templateId, "Sample Alarm Template","Sample Alarm Template");
            } else {
                return null;
            }
        };
        return new JdbcAlarmTemplateProvider(function);

    }
}
