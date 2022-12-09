package com.example.service;

import com.example.vo.UserNameAndPassword;

/**
 * @Author: wgs
 * @Date 2022/11/8 15:39
 * @Classname IJWTService
 * @Description JWT 相关服务类定义
 */
public interface IJWTService {
    /**
     * 生成 JWT TOKEN
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    String generateToken(String username, String password) throws Exception;

    /**
     * 生成指定超时时间 Token, 单位天
     * @param username
     * @param password
     * @param expire
     * @return
     * @throws Exception
     */
    String generateToken(String username, String password, int expire) throws Exception;

    /**
     * 注册用户并生成 TOKEN
     * @param usernameAndPassword
     * @return
     * @throws Exception
     */
    String registerUserAndGenerateToken(UserNameAndPassword usernameAndPassword) throws Exception;
}
