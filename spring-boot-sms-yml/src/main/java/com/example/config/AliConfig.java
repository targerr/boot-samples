package com.example.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2023/8/25 10:45
 * @Classname AliConfig
 * @Description
 */
@RequiredArgsConstructor
@Configuration
public class AliConfig {

    private final AppProperties appProperties;

    @Bean
    public IAcsClient iAcsClient() {
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou",
                appProperties.getAli().getApiKey(),
                appProperties.getAli().getApiSecret());
        return new DefaultAcsClient(profile);
    }
}
