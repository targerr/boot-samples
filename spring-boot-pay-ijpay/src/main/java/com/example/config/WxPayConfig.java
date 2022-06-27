package com.example.config;

import com.example.entity.WxPayBean;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/6/24 10:47
 * @Classname WxPayConfig
 * @Description
 */
@Configuration
public class WxPayConfig {
    @Autowired
    WxPayBean wxPayBean;

    @Bean
    public WxPayApiConfig wxPayApiConfig() {
        WxPayApiConfig wxPayApiConfig = WxPayApiConfig.builder()
                .appId(wxPayBean.getAppId())
                .mchId(wxPayBean.getMchId())
                .partnerKey(wxPayBean.getPartnerKey())
                .certPath(wxPayBean.getCertPath())
                .domain(wxPayBean.getDomain())
                .build();
        // 放入缓存
        WxPayApiConfigKit.setThreadLocalWxPayApiConfig(wxPayApiConfig);
        return wxPayApiConfig;
    }

}
