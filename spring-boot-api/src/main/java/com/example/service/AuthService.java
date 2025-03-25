package com.example.service;


import com.example.entity.OAuth2Token;

/**
 * 授权协议
 */
public interface AuthService {
    /**
     * 提供有效的token
     * @return
     */
    OAuth2Token getToken();
}
