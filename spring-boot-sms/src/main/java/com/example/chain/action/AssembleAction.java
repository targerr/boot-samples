package com.example.chain.action;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.chain.domain.MessageParam;
import com.example.chain.domain.SendTaskModel;
import com.example.chain.domain.TaskInfo;
import com.example.chain.domain.pojo.MessageTemplate;
import com.example.chain.enums.ChannelType;
import com.example.chain.enums.RespStatusEnum;
import com.example.chain.pipeline.BusinessProcess;
import com.example.chain.pipeline.ProcessContext;
import com.example.chain.vo.BasicResultVO;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.ContentModel;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author 3y
 * @date 2021/11/22
 * @description 拼装参数
 */
@Slf4j
@Service
public class AssembleAction implements BusinessProcess<SendTaskModel> {


    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        log.debug("组装参数: context {}", JSONObject.toJSONString(context, true));

        SendTaskModel sendTaskModel = context.getProcessModel();
        Long messageTemplateId = sendTaskModel.getMessageTemplateId();
        if (messageTemplateId != null){
            return;
        }

        try {
            MessageTemplate messageTemplate = new MessageTemplate();
//            if (!messageTemplate.isPresent() || messageTemplate.get().getIsDeleted().equals(AustinConstant.TRUE)) {
//                context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.TEMPLATE_NOT_FOUND));
//                return;
//            }
//            if (BusinessCode.COMMON_SEND.getCode().equals(context.getCode())) {
//                List<TaskInfo> taskInfos = assembleTaskInfo(sendTaskModel, messageTemplate.get());
//                sendTaskModel.setTaskInfo(taskInfos);
//            } else if (BusinessCode.RECALL.getCode().equals(context.getCode())) {
//                sendTaskModel.setMessageTemplate(messageTemplate.get());
//            }
        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR));
            log.error("assemble task fail! templateId:{}, e:{}", messageTemplateId, Throwables.getStackTraceAsString(e));
        }

    }

    /**
     * 组装 TaskInfo 任务消息
     *
     * @param sendTaskModel
     * @param messageTemplate
     */
    private List<TaskInfo> assembleTaskInfo(SendTaskModel sendTaskModel, MessageTemplate messageTemplate) {
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();
        List<TaskInfo> taskInfoList = new ArrayList<>();

        for (MessageParam messageParam : messageParamList) {

            // 组装参数
            TaskInfo taskInfo = TaskInfo.builder()
                    .receiver(new HashSet<>(Arrays.asList(messageParam.getReceiver().split(String.valueOf(StrUtil.C_COMMA)))))
                    .contentModel(getContentModelValue(messageTemplate, messageParam)).build();

            taskInfoList.add(taskInfo);
        }

        return taskInfoList;

    }


    /**
     * 获取 contentModel，替换模板msgContent中占位符信息
     */
    private static ContentModel getContentModelValue(MessageTemplate messageTemplate, MessageParam messageParam) {

        // 得到真正的ContentModel 类型
        Integer sendChannel = messageTemplate.getSendChannel();
        Class contentModelClass = ChannelType.getChanelModelClassByCode(sendChannel);


        // 得到模板的 msgContent 和 入参
        Map<String, String> variables = messageParam.getVariables();
        JSONObject jsonObject = JSON.parseObject(messageTemplate.getMsgContent());


        return new ContentModel();
    }
}
