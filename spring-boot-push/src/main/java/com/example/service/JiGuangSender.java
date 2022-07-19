package com.example.service;

import cn.hutool.core.util.StrUtil;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSONObject;
import com.example.exception.PushException;
import com.example.param.SendRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: wgs
 * @Date 2022/7/18 14:47
 * @Classname JiGuangSender
 * @Description 极光推送
 */
@Service
@Slf4j
public class JiGuangSender extends BasePushSender {
    @Autowired
    private JPushClient jPushClient;

    @Override
    public PushSenderEnum getPushSenderEnum() {
        return PushSenderEnum.JI_GUANG;
    }

    @Override
    protected void validate(SendRequest sendRequest) {
        if (sendRequest == null || StrUtil.isEmpty(sendRequest.getCid())) {
            log.debug("【极光】推送参数不合法 data: {}", JSONObject.toJSONString(sendRequest, true));
            throw new PushException(104, "【极光】推送参数不合法");
        }
    }

    @Override
    protected void execute(SendRequest sendRequest) {
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(sendRequest.getCid()))
                .setNotification(Notification.alert(sendRequest.getMessageParam().getBody()))
                .build();

        PushPayload.Builder builder = PushPayload.newBuilder();
//        if ("1".equals(sendRequest.getTokenType())) {
//            builder.setNotification(Notification.android(sendRequest.getMessageParam().getBody(), sendRequest.getMessageParam().getTitle(), null));
//        }
//        if ("0".equals(sendRequest.getTokenType())) {
//            builder.setNotification(Notification.ios(sendRequest.getMessageParam().getBody(), null));
//        }
        try {
            PushResult result = jPushClient.sendPush(payload);
            log.info("Got result - " + JSONObject.toJSONString(result, true));
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
            log.error("Sendno: " + payload.getSendno());

        } catch (APIRequestException e) {
            log.error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
            log.error("Sendno: " + payload.getSendno());
        }
    }

    @Override
    protected void console(SendRequest sendRequest) {
        log.debug("【极光】推送参数 data: {}", JSONObject.toJSONString(sendRequest, true));
    }

}
