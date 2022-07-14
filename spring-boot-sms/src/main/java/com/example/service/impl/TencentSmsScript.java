package com.example.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.example.annotation.SmsScriptHandler;
import com.example.config.properties.TencentSmsProperties;
import com.example.enums.SmsEnum;
import com.example.service.BaseSmsScript;
import com.example.service.SmsScript;
import com.google.common.base.Throwables;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @Author: wgs
 * @Date 2022/7/14 14:41
 * @Classname TencentSmsScript
 * 1. 发送短信接入文档：https://cloud.tencent.com/document/api/382/55981
 * 2. 推荐直接使用SDK调用
 * 3. 推荐使用API Explorer 生成代码
 */
@Slf4j
@SmsScriptHandler("TencentSmsScript")
public class TencentSmsScript extends BaseSmsScript {
    @Override
    public void send(String phone, String captcha, SmsEnum smsEnum) {
        try {
            log.info("【腾讯短信】发送!!");
            TencentSmsProperties tencentSmsProperties = new TencentSmsProperties();
            SmsClient client = init(tencentSmsProperties);
//            SendSmsRequest request = assembleReq(smsParam, tencentSmsProperties);
//            SendSmsResponse response = client.SendSms(request);
//             assembleSmsRecord(smsParam, response, tencentSmsProperties);
        } catch (Exception e) {
            log.error("TencentSmsScript#send fail:{}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 组装发送短信参数
     */
//    private SendSmsRequest assembleReq(SmsParam smsParam, TencentSmsAccount account) {
//        SendSmsRequest req = new SendSmsRequest();
//        String[] phoneNumberSet1 = smsParam.getPhones().toArray(new String[smsParam.getPhones().size() - 1]);
//        req.setPhoneNumberSet(phoneNumberSet1);
//        req.setSmsSdkAppId(account.getSmsSdkAppId());
//        req.setSignName(account.getSignName());
//        req.setTemplateId(account.getTemplateId());
//        String[] templateParamSet1 = {smsParam.getContent()};
//        req.setTemplateParamSet(templateParamSet1);
//        req.setSessionContext(IdUtil.fastSimpleUUID());
//        return req;
//    }

    /**
     * 初始化 client
     *
     * @param account
     */
    private SmsClient init(TencentSmsProperties account) {
        Credential cred = new Credential(account.getSecretId(), account.getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(account.getUrl());
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        SmsClient client = new SmsClient(cred, account.getRegion(), clientProfile);
        return client;
    }
}
