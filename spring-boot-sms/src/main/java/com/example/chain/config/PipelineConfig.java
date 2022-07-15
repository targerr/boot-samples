package com.example.chain.config;

import com.example.chain.action.AfterParamCheckAction;
import com.example.chain.action.AssembleAction;
import com.example.chain.action.PreParamCheckAction;
import com.example.chain.action.SendMqAction;
import com.example.chain.process.ProcessController;
import com.example.chain.process.ProcessTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/7/15 10:37
 * @Classname PipelineConfig
 * @Description
 */
@Slf4j
@Configuration
public class PipelineConfig {
    @Autowired
    private PreParamCheckAction preParamCheckAction;
    @Autowired
    private AssembleAction assembleAction;
    @Autowired
    private AfterParamCheckAction afterParamCheckAction;
    @Autowired
    private SendMqAction sendMqAction;

    @Bean("commonSendTemplate")
    public ProcessTemplate commonSendTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        processTemplate.setProcessList(Arrays.asList(preParamCheckAction, assembleAction, afterParamCheckAction, sendMqAction));
        return processTemplate;
    }

    @Bean
    public ProcessController processController() {
        ProcessController processController = new ProcessController();
        Map<String, ProcessTemplate> templateConfig = new HashMap<>(4);
        templateConfig.put("send", commonSendTemplate());
        processController.setTemplateConfig(templateConfig);
        return processController;
    }

}
