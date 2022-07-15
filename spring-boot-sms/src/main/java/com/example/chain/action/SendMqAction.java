package com.example.chain.action;

import com.alibaba.fastjson.JSONObject;
import com.example.chain.domain.SendTaskModel;
import com.example.chain.domain.TaskInfo;
import com.example.chain.event.BusinessEvent;
import com.example.chain.pipeline.BusinessProcess;
import com.example.chain.pipeline.ProcessContext;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: wgs
 * @Date 2022/5/26 10:20
 * @Classname SendMqAction
 * @Description
 */
@Slf4j
@Service
public class SendMqAction implements BusinessProcess<SendTaskModel> {
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        log.debug("发送消息: context {}", JSONObject.toJSONString(context, true));

        SendTaskModel sendTaskModel = context.getProcessModel();
        Long messageTemplateId = sendTaskModel.getMessageTemplateId();
        //TODO 使用mq发送
        log.debug("使用mq发送消息: context {}", JSONObject.toJSONString(context, true));

        List<TaskInfo> taskInfoList = sendTaskModel.getTaskInfo();
        for (TaskInfo taskInfo : taskInfoList){
            // 构造事件对象
            ApplicationEvent event = new BusinessEvent(taskInfo);
            //发布事件
            eventPublisher.publishEvent(event);
            log.error("发布事件");


        }


    }
}
