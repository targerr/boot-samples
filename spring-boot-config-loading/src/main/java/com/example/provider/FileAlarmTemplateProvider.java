package com.example.provider;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.config.TemplateConfig;
import com.example.entity.AlarmTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: wgs
 * @Date 2025/1/14 17:06
 * @Classname FileAlarmTemplateProvider
 * @Description
 */
@Service
public class FileAlarmTemplateProvider extends BaseAlarmTemplateProvider {

    private final static String HTTP = "http";
    private final static String HTTPS = "https";

    private final TemplateConfig templateConfig;
    private final Map<String, AlarmTemplate> configTemplateMap;

    public FileAlarmTemplateProvider(TemplateConfig templateConfig) {
        this.templateConfig = templateConfig;
        this.configTemplateMap =new HashMap<String, AlarmTemplate>();
    }

    @Override
    public AlarmTemplate getAlarmTemplate(String templateId) {
        AlarmTemplate alarmTemplate = configTemplateMap.get(templateId);
        if (ObjectUtils.isEmpty(alarmTemplate)) {
            String templatePath = templateConfig.getTemplatePath();
            String templateContent;
            if (StringUtils.startsWithIgnoreCase(templatePath, HTTP) || StringUtils.startsWithIgnoreCase(templatePath, HTTPS)) {
                URL url = URLUtil.url(templatePath);
                File file = FileUtil.file(url);
                FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);
                templateContent = fileReader.readString();
            } else {
                templateContent = ResourceUtil.readUtf8Str(templatePath);
            }
            List<AlarmTemplate> alarmTemplateList = JSONObject.parseArray(templateContent, AlarmTemplate.class);
            Map<String, AlarmTemplate> templateMap = alarmTemplateList.stream()
                    .collect(Collectors.toMap(AlarmTemplate::getTemplateId, k -> k));
            configTemplateMap.putAll(templateMap);
            alarmTemplate = templateMap.get(templateId);
            if (ObjectUtils.isEmpty(alarmTemplate)) {
                throw new RuntimeException("未发现告警配置模板");
            }
        }
        return alarmTemplate;
    }
}
