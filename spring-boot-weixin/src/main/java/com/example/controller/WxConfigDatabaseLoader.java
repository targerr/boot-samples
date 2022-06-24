package com.example.controller;

import com.example.config.WxConfig;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.wxaapp.WxaConfig;
import net.dreamlu.weixin.config.WxConfigLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * 微信配置加载器，用于自定义实现
 */
@Configuration
public class WxConfigDatabaseLoader implements WxConfigLoader {
    @Autowired
    private WxConfig wxConfig;

    @Override
    public List<ApiConfig> loadWx() {
        ApiConfig wxConf = new ApiConfig();
        wxConf.setAppId(wxConfig.getAppId());
        wxConf.setAppSecret(wxConfig.getAppSecret());
        wxConf.setToken(wxConfig.getToke());
        return Collections.singletonList(wxConf);
    }

    @Override
    public List<WxaConfig> loadWxa() {
        return Collections.emptyList();
    }
}
