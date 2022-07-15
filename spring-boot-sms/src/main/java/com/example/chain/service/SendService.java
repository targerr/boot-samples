package com.example.chain.service;

import com.example.chain.domain.SendRequest;
import com.example.chain.domain.SendResponse;

/**
 * @Author: wgs
 * @Date 2022/7/14 16:51
 * @Classname SendService
 * @Description 发送接口
 */
public interface SendService {
    /**
     * 单文案发送接口
     * @param sendRequest
     * @return
     */
    SendResponse send(SendRequest sendRequest);

}
