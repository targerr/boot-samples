package com.example;

import cn.jpush.api.JPushClient;
import com.example.properties.GetuiProperties;
import com.example.properties.JpushProperties;
import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.api.UserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/7/18 13:51
 * @Classname PushConfig
 * @Description 配置类
 */
@Configuration
public class PushConfig {
    @Autowired
    private GetuiProperties getuiConfig;
    @Autowired
    private JpushProperties jpushConfig;

    @Bean
    public PushApi pushApi() {
        // 设置httpClient最大连接数，当并发较大时建议调大此参数。
        System.setProperty("http.maxConnections", "200");
        GtApiConfiguration apiConfiguration = getGtApiConfiguration();

        ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
        return apiHelper.creatApi(PushApi.class);
    }


    @Bean
    public UserApi userApi() {
        GtApiConfiguration apiConfiguration = getGtApiConfiguration();
        apiConfiguration.setMaxHttpTryTime(0);
        apiConfiguration.setOpenCheckHealthDataSwitch(true);
        apiConfiguration.setOpenAnalyseStableDomainSwitch(true);
        apiConfiguration.setSoTimeout(5000);
        apiConfiguration.setConnectTimeout(5000);

        ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
        return apiHelper.creatApi(UserApi.class);
    }

    private GtApiConfiguration getGtApiConfiguration() {
        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
        // 填写应用配置
        apiConfiguration.setAppId(getuiConfig.getAppId());
        apiConfiguration.setAppKey(getuiConfig.getAppKey());
        apiConfiguration.setMasterSecret(getuiConfig.getMasterSecret());
        apiConfiguration.setDomain("https://restapi.getui.com/v2/");
        return apiConfiguration;
    }

    @Bean
    public JPushClient jPushClient() {
        return new JPushClient( jpushConfig.getMasterSecret(),jpushConfig.getAppKey());
    }

}
