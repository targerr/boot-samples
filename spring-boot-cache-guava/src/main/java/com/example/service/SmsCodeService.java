package com.example.service;

import com.google.common.cache.Cache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Random;

/**
 * @Author: wgs
 * @Date 2023/7/4 17:59
 * @Classname SmsCodeService
 * @Description
 */
@Slf4j
@Component
@AllArgsConstructor
public class SmsCodeService {

    private final Cache<String, String> smsCache;

    /**
     * 短信验证码发送
     */
    public void smsCodeSend(String phone) {

        String smsCode = smsCache.getIfPresent(phone);
        if(!StringUtils.isEmpty(smsCode)){
            throw new RuntimeException("操作过于频繁，请稍后再试");
        }
        String code = smsCodeGenerator();
        smsCache.put(phone, code);
        log.info("短信验证码生成 手机号:[{}] 验证码:[{}] ", phone, code);

        /* 调用阿里云发送短信 */
      //  sendSms.send(phone,code);
    }


    /**
     * 短信验证码校验
     * */
    public boolean smsChecked(String phone,String smsCode){
        boolean flag = false;
        try {

            String code = smsCache.getIfPresent(phone);

            if(!StringUtils.isEmpty(code) && smsCode.equals(code)){
                log.info("短信验证码校验成功");
                smsCache.invalidate(phone);
                flag = true;
            }


        }catch (Exception e){
            log.info("验证码错误 [{}]",e.getMessage());
            throw new RuntimeException("验证码错错误");
        }

        log.info("缓存统计信息：[{}]", smsCache.stats());

        return flag;
    }


    /**
     * 短信验证码 生成规则
     * */
    private String smsCodeGenerator(){
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
}
