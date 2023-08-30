package com.example.service.impl;

import cn.leancloud.sms.AVSMS;
import cn.leancloud.sms.AVSMSOption;
import com.example.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: wgs
 * @Date 2023/8/25 10:58
 * @Classname SmsServiceLeanCloudSmsImpl
 * @Description
 */
@RequiredArgsConstructor
@Slf4j
@Service
@ConditionalOnProperty(prefix = "mooc.sms-provider", name = "name", havingValue = "lean-cloud")
public class SmsServiceLeanCloudSmsImpl implements SmsService {

    @Override
    public void send(String mobile, String msg) {
        val option = new AVSMSOption();
        option.setTtl(10);
        option.setApplicationName("测试实战Spring Security");
        option.setOperation("两步验证");
        option.setTemplateName("登录验证");
        option.setSignatureName("测试");
        option.setType(AVSMS.TYPE.TEXT_SMS);
        option.setEnvMap(Map.of("smsCode", msg));
        AVSMS.requestSMSCodeInBackground(mobile, option)
                .take(1)
                .subscribe(
                        (res) -> log.info("短信发送成功 {}", res),
                        (err) -> log.error("发送短信时产生服务端异常 {}", err.getLocalizedMessage())
                );
    }
}