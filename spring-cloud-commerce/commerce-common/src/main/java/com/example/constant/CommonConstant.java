package com.example.constant;

/**
 * @Author: wgs
 * @Date 2022/11/7 11:21
 * @Classname CommonConstant
 * @Description 通用模块常量定义
 */
public interface CommonConstant {
    /** RSA 公钥 */
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzMuLcnmFEww1g7BSnbx5esu8L8X1WsUsz60ybsH958Pb0ncyFYKnA4+GibYzeOLE3ruC0Qbh9RRp0mcOYLtN7HJft/SFSH9QBnKvRXyDkPK3ZY96EUNSBVrtLtMVBc2MzFNfJ1dd2/oOe8FXp5YTVoL2E2wKp64LAg5uoYjKLLkpRicWlHx2/H+ItlhP/7sSGz6db7bGl/6xX65ZxPJRqGquQm2/ima46T3MuhaSv+IxjlrWCtJf4s1lTTXqc8V0Bly6nwq5yDaiuJVutcBn+M9OTHbKmdlpXy0fWgRLyqkQAntGbBw6egZE2RyM7qkm0AF22sHzLmiSPCy8Pl4MKQIDAQAB";

    /** JWT 中存储用户信息的 key */
    public static final String JWT_USER_INFO_KEY = "commerce-user";

    /** 授权中心的 service-id */
    public static final String AUTHORITY_CENTER_SERVICE_ID = "commerce-authority-center";
}
