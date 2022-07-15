package com.example.chain.action;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.ReUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.chain.domain.SendTaskModel;
import com.example.chain.domain.TaskInfo;
import com.example.chain.enums.RespStatusEnum;
import com.example.chain.pipeline.BusinessProcess;
import com.example.chain.pipeline.ProcessContext;
import com.example.chain.vo.BasicResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: wgs
 * @Date 2022/5/26 10:19
 * @Classname AfterParamCheckAction
 * @Description 后置检查
 */
@Slf4j
@Service
public class AfterParamCheckAction implements BusinessProcess<SendTaskModel> {
    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        log.debug("后置参数检查: context {}", JSONObject.toJSONString(context, true));

        SendTaskModel sendTaskModel = context.getProcessModel();
        List<TaskInfo> taskInfo = sendTaskModel.getTaskInfo();

        Long messageTemplateId = sendTaskModel.getMessageTemplateId();

        // 测试 直接放行
        if (messageTemplateId != null){
            return;
        }

        // 1. 过滤掉不合法的手机号、邮箱
        filterIllegalReceiver(taskInfo);


        if (CollUtil.isEmpty(taskInfo)) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.FAIL));
        }
    }

    /**
     * 如果指定类型是手机号，检测输入手机号是否合法
     * 如果指定类型是邮件，检测输入邮件是否合法
     *
     * @param taskInfo
     */
    private void filterIllegalReceiver(List<TaskInfo> taskInfo) {
       // Integer idType = CollUtil.getFirst(taskInfo.iterator()).getIdType();
        filter(taskInfo);
    }

    /**
     * 利用正则过滤掉不合法的接收者
     *
     * @param taskInfo
     */
    private void filter(List<TaskInfo> taskInfo) {
        Iterator<TaskInfo> iterator = taskInfo.iterator();
        while (iterator.hasNext()) {
            TaskInfo task = iterator.next();
            Set<String> illegalPhone = task.getReceiver().stream()
                    // 不合法
                    .filter(phone -> !PhoneUtil.isPhone(phone))
                    .collect(Collectors.toSet());

            if (CollUtil.isNotEmpty(illegalPhone)) {
                task.getReceiver().removeAll(illegalPhone);
                log.error("messageTemplateId:{} find illegal receiver!{}", task.getMessageTemplateId(), JSON.toJSONString(illegalPhone));
            }
            if (CollUtil.isEmpty(task.getReceiver())) {
                iterator.remove();
            }
        }
    }

}
