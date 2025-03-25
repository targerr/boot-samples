package com.example.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import com.alibaba.fastjson2.JSON;
import com.example.configuration.ApiConfig;
import com.example.entity.OAuth2Token;
import com.example.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;


/**
 * 授权协议
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private ApiConfig apiConfig;

    private OAuth2Token tokenData;

    public OAuth2Token oAuthToken() {
        String authorization = apiConfig.getClientId() + ":" + apiConfig.getClientSecret() + ":" + System.currentTimeMillis();
        SM2 sm2 = SmUtil.sm2(null, apiConfig.getPublicKey());
        String base64 = Base64.encode(sm2.encrypt(authorization.getBytes(StandardCharsets.UTF_8), KeyType.PublicKey));
        String authUrl = apiConfig.getUrl() + "/oauth/api-token";
        RestTemplate restTemplate = new RestTemplate();
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", base64);
        // 发送请求
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(authUrl, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            tokenData = JSON.parseObject(response.getBody(), OAuth2Token.class);
            log.info("授权成功: {}", response.getBody());
            tokenData.setCreateTime(System.currentTimeMillis());
            OAuth2Token tokenData = JSON.parseObject(response.getBody(), OAuth2Token.class);
            tokenData.setCreateTime(System.currentTimeMillis());
            return tokenData;
        } else {
            log.info("授权失败: {}", response.getBody());
            throw new RuntimeException("授权失败" + response.getBody());
        }
    }

    @Override
    public OAuth2Token getToken() {
        synchronized (this) {
            long nowTime = System.currentTimeMillis();
            if (tokenData == null
                    ||
                    nowTime - tokenData.getCreateTime() >= tokenData.getExpiresIn() * 1000
            ) {
                oAuthToken();
            }
            return tokenData;
        }
    }

}
