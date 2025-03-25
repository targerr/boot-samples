package com.example.entity;

import java.io.Serializable;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class OAuth2Token implements Serializable {
    @JSONField(name = "access_token")
    private String accessToken;
    @JSONField(name = "refresh_token")
    private String refreshToken;
    @JSONField(name = "client_id")
    private String clientId;
    @JSONField(name = "user_name")
    private String userName;
    @JSONField(name = "expires_in")
    private Integer  expiresIn;
    private Long createTime;
}