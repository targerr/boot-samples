package com.example.service;

import com.example.param.SendRequest;

/**
 * @Author: wgs
 * @Date 2022/7/18 14:34
 * @Classname BasePushSender
 * @Description 模板
 */
public abstract class BasePushSender implements PushSender{
    /**
     * 推送单条
     */
    @Override
    public void singleMsg(SendRequest sendRequest) {
        console(sendRequest);
        validate(sendRequest);
        execute(sendRequest);
    }

    /**
     * 验证消息
     * @param sendRequest
     */
    protected abstract void validate(SendRequest sendRequest);

    /**
     * 发送消息
     *
     * @param sendRequest
     */
    protected abstract void execute(SendRequest sendRequest);

    /**
     * 打印日志
     *
     * @param sendRequest
     */
    protected abstract void console(SendRequest sendRequest);


}
