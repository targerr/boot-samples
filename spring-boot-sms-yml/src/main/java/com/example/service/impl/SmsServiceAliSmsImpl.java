package com.example.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.example.config.AppProperties;
import com.example.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @Author: wgs
 * @Date 2023/8/25 10:56
 * @Classname SmsServiceAliSmsImpl
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "wgs.sms-provider", name = "name", havingValue = "ali")
@Service
public class SmsServiceAliSmsImpl implements SmsService {

    private final IAcsClient client;
    private final AppProperties appProperties;

    @Override
    public void send(String mobile, String msg) {
        val request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(appProperties.getSmsProvider().getApiUrl());
        request.setSysAction("SendSms");
        request.setSysVersion("2017-05-25");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", "登录验证");
        request.putQueryParameter("TemplateCode", "SMS_1610048");
        request.putQueryParameter("TemplateParam", "{\"code\":\"" +
                msg +
                "\",\"product\":\"测试\"}");
        try {
            val response = client.getCommonResponse(request);
            log.info("短信发送结果 {}", response.getData());
        } catch (RuntimeException e) {
            log.error("发送短信时产生服务端异常 {}", e.getLocalizedMessage());
        } catch (ClientException e) {
            log.error("发送短信时产生客户端异常 {}", e.getLocalizedMessage());
        }
    }
}
